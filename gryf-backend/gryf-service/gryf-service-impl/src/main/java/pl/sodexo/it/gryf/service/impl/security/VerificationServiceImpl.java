package pl.sodexo.it.gryf.service.impl.security;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.dao.individuals.IndividualUserDao;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.service.local.api.MailService;

/**
 * Implementacja serwisu obsługującego zdarzenia związane z weryfikacją osoby fizycznej
 *
 * Created by akmiecinski on 17.10.2016.
 */
//TODO: AdamK
@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private MailService mailService;

    @Autowired
    private IndividualSearchDao individualSearchDao;

    @Autowired
    private IndividualUserDao individualUserDao;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    public void resendVerificationCode(VerificationDto verificationDto) throws GryfVerificationException {
        validateVerificationData(verificationDto);
        prepareNewVerificationCode(verificationDto);
    }

    //TODO: gdy będzie analiza jak mamy generować kod
    @Override
    public String createVerificationCode() {
        return RandomStringUtils.randomAlphabetic(applicationParameters.getVerificationCodeLength());
    }

    private void validateVerificationData(VerificationDto verificationDto) throws GryfVerificationException {
        validatePesel(verificationDto);
        validateEmail(verificationDto);
        checkEmailPeselPairCorrectness(verificationDto);
    }

    private void validateEmail(VerificationDto verificationDto) throws GryfVerificationException {
        if(!EmailValidator.getInstance().isValid(verificationDto.getEmail()))
            throw new GryfVerificationException("Niepoprawny email");

    }

    private void validatePesel(VerificationDto verificationDto) throws GryfVerificationException {
        if(!PeselUtils.validate(verificationDto.getPesel()))
            throw new GryfVerificationException("Niepoprawna wartość PESEL");

    }

    private void checkEmailPeselPairCorrectness(VerificationDto verificationDto) throws GryfVerificationException {
        if (individualSearchDao.findIndividualIdByPeselAndEmail(verificationDto) == null)
            throw new GryfVerificationException("Niepoprawna para PESEL - adres email");
    }

    private String prepareNewVerificationCode(VerificationDto verificationDto){
        IndividualUser user = individualUserDao.findByPeselAndEmail(verificationDto.getPesel(), verificationDto.getEmail());
        String newVerificationCode = createVerificationCode();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setVerificationCode(passwordEncoder.encode(newVerificationCode));
        individualUserDao.save(user);
        return newVerificationCode;
    }

    private void sendMailNotification(){

    }

    //    private MailDTO createMail(String pesel, String newVerificationCode){
    //        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("verificationCode", newVerificationCode);
    //        return mailService.createMailDTO(EmailTemplate.VC_SEND,
    //                mailPlaceholders)
    //    }


}
