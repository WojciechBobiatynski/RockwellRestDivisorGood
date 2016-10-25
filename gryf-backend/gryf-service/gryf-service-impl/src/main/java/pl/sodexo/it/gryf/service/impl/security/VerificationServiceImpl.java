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
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.common.exception.authentication.GryfUserNotActiveException;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.local.api.MailService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.EMAIL_BODY_VER_CODE_PLACEHOLDER;
import static pl.sodexo.it.gryf.common.utils.GryfConstants.VERIFICATION_CODE_EMAIL_TEMPLATE_CODE;

/**
 * Implementacja serwisu obsługującego zdarzenia związane z weryfikacją osoby fizycznej
 *
 * Created by akmiecinski on 17.10.2016.
 */
@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private IndividualUserService individualUserService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Override
    public void resendVerificationCode(VerificationDto verificationDto) throws GryfVerificationException {
        GryfIndUserDto user = validateVerificationData(verificationDto);
        //TODO: na razie tworzymy nowy kod, docelowo ma być szyfrowany i odszyfrowywany
        String newVerificationCode = createAndSaveNewVerificationCode(user);
        sendMailNotification(verificationDto, newVerificationCode);
    }

    private GryfIndUserDto validateVerificationData(VerificationDto verificationDto) throws GryfVerificationException {
        validatePesel(verificationDto);
        validateEmail(verificationDto);
        return checkEmailPeselPairCorrectness(verificationDto);
    }

    private void validateEmail(VerificationDto verificationDto) throws GryfVerificationException {
        if (!EmailValidator.getInstance().isValid(verificationDto.getEmail()))
            throw new GryfVerificationException("Niepoprawny email");

    }

    private void validatePesel(VerificationDto verificationDto) throws GryfVerificationException {
        if (!PeselUtils.validate(verificationDto.getPesel()))
            throw new GryfVerificationException("Niepoprawna wartość PESEL");

    }

    private GryfIndUserDto checkEmailPeselPairCorrectness(VerificationDto verificationDto) throws GryfVerificationException {
        GryfIndUserDto user = individualUserService.findByPesel(verificationDto.getPesel());

        if (user == null)
            throw new GryfVerificationException("Nie znaleziono użytkownika o podanym numerze PESEL");

        checkUserBlockedAndUnlock(user);

        if(!user.isActive())
            throw new GryfUserNotActiveException("Twoje konto jest nieaktywne. Zgłoś sie do administratora");

        if(!verificationDto.getEmail().equals(user.getVerificationEmail())){
            user.setLastResetFailureDate(new Date());
            user.setResetFailureAttempts(user.getResetFailureAttempts() + 1);
            if (user.getResetFailureAttempts() >= applicationParameters.getMaxIndResetFailureAttempts()) {
                user.setActive(false);
            }
            individualUserService.saveAndFlushIndUserInNewTransaction(user);
            throw new GryfVerificationException("Niepoprawna para PESEL - adres email");
        }
        user.setResetFailureAttempts(GryfConstants.DEFAULT_RESET_FAILURE_ATTEMPTS_NUMBER);
        individualUserService.saveIndUser(user);

        return user;
    }

    private GryfIndUserDto checkUserBlockedAndUnlock(GryfIndUserDto user) {
        if (user.getLastResetFailureDate() == null) {
            return user;
        }

        LocalDateTime lastResetFailureDate = LocalDateTime.ofInstant(user.getLastResetFailureDate().toInstant(), ZoneId.systemDefault());
        if (lastResetFailureDate.plusMinutes(applicationParameters.getIndUserResetBlockMinutes()).isBefore(LocalDateTime.now()) && user.getResetFailureAttempts() >= applicationParameters
                .getMaxIndResetFailureAttempts()) {
            user.setActive(true);
            user.setResetFailureAttempts(GryfConstants.DEFAULT_RESET_FAILURE_ATTEMPTS_NUMBER);
        }
        return user;
    }

    @Override
    public String createAndSaveNewVerificationCode(GryfIndUserDto gryfIndUserDto) {
        String newVerificationCode = createVerificationCode();
        //TODO: usunąć LOGGERA gdy wysyłka maila będzie działać oraz analiza generacji kodu będzie kompletna
        LOGGER.info("Nowe hasło, Pesel={}, Email={}, Hasło={}", gryfIndUserDto.getPesel(), gryfIndUserDto.getVerificationEmail(), newVerificationCode);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        gryfIndUserDto.setVerificationCode(passwordEncoder.encode(newVerificationCode));
        individualUserService.saveIndUser(gryfIndUserDto);
        return newVerificationCode;
    }

    //TODO: gdy będzie analiza jak mamy generować kod
    private String createVerificationCode() {
        return RandomStringUtils.randomAlphabetic(applicationParameters.getVerificationCodeLength());
    }

    private void sendMailNotification(VerificationDto verificationDto, String verificationCode) {
        mailService.scheduleMail(createMailDTO(verificationDto, verificationCode));
    }

    private MailDTO createMailDTO(VerificationDto verificationDto, String verificationCode) {
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
