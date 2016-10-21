package pl.sodexo.it.gryf.service.impl.security;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.dao.individuals.IndividualUserDao;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.service.local.api.MailService;

import java.util.Collections;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.EMAIL_BODY_VER_CODE_PLACEHOLDER;
import static pl.sodexo.it.gryf.common.utils.GryfConstants.VERIFICATION_CODE_EMAIL_TEMPLATE_CODE;

/**
 * Implementacja serwisu obsługującego zdarzenia związane z weryfikacją osoby fizycznej
 *
 * Created by akmiecinski on 17.10.2016.
 */
//TODO: AdamK
@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private IndividualSearchDao individualSearchDao;

    @Autowired
    private IndividualUserDao individualUserDao;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Override
    public void resendVerificationCode(VerificationDto verificationDto) throws GryfVerificationException {
        validateVerificationData(verificationDto);
        String newVerificationCode = createAndSaveNewVerificationCode(verificationDto);
        sendMailNotification(verificationDto, newVerificationCode);
    }

    @Override
    public String createAndSaveNewVerificationCode(VerificationDto verificationDto) {
        IndividualUser user = individualUserDao.findByPeselAndEmail(verificationDto.getPesel(), verificationDto.getEmail());
        String newVerificationCode = createVerificationCode();
        //TODO: usunąć LOGGERA gdy wysyłka maila będzie działać oraz analiza generacji kodu będzie kompletna
        LOGGER.info("Nowe hasło, Pesel={}, Email={}, Hasło={}", verificationDto.getPesel(), verificationDto.getEmail(), newVerificationCode);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setVerificationCode(passwordEncoder.encode(newVerificationCode));
        individualUserDao.save(user);
        return newVerificationCode;
    }

    //TODO: gdy będzie analiza jak mamy generować kod
    private String createVerificationCode() {
        return RandomStringUtils.randomAlphabetic(applicationParameters.getVerificationCodeLength());
    }

    private void validateVerificationData(VerificationDto verificationDto) throws GryfVerificationException {
        validatePesel(verificationDto);
        validateEmail(verificationDto);
        checkEmailPeselPairCorrectness(verificationDto);
    }

    private void validateEmail(VerificationDto verificationDto) throws GryfVerificationException {
        if (!EmailValidator.getInstance().isValid(verificationDto.getEmail()))
            throw new GryfVerificationException("Niepoprawny email");

    }

    private void validatePesel(VerificationDto verificationDto) throws GryfVerificationException {
        if (!PeselUtils.validate(verificationDto.getPesel()))
            throw new GryfVerificationException("Niepoprawna wartość PESEL");

    }

    private void checkEmailPeselPairCorrectness(VerificationDto verificationDto) throws GryfVerificationException {
        if (individualSearchDao.findIndividualIdByPeselAndEmail(verificationDto) == null)
            throw new GryfVerificationException("Niepoprawna para PESEL - adres email");
    }

    private void sendMailNotification(VerificationDto verificationDto, String verificationCode) {
        mailService.scheduleMail(createMailDTO(verificationDto, verificationCode));
    }

    private MailDTO createMailDTO(VerificationDto verificationDto, String verificationCode){
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_VER_CODE_PLACEHOLDER, verificationCode);
        EmailTemplate emailTemplate = emailTemplateRepository.get(VERIFICATION_CODE_EMAIL_TEMPLATE_CODE);
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(verificationDto.getEmail());
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }

}
