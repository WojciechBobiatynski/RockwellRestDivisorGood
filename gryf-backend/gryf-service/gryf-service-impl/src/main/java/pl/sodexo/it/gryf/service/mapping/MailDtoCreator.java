package pl.sodexo.it.gryf.service.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.security.individuals.Verifiable;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.local.api.MailService;

import java.util.Collections;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.*;

/**
 * Klasa tworząca DTO dla maili
 *
 * Created by akmiecinski on 28.10.2016.
 */
@Component
public class MailDtoCreator {

    @Autowired
    private MailService mailService;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    public MailDTO createMailDTOForResetLink(String email, String newLink, String contextPath) {
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_RESET_LINK_PLACEHOLDER, contextPath + RESET_LINK_URL_PREFIX + newLink);
        EmailTemplate emailTemplate = emailTemplateRepository.get(RESET_LINK_EMAIL_TEMPLATE_CODE);
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }

    public MailDTO createMailDTOForVerificationCode(Verifiable verifiable, String appUrl) {
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_VER_CODE_PLACEHOLDER, verifiable.getVerificationCode()).add(EMAIL_BODY_LOGIN_PLACEHOLDER, verifiable.getLogin())
                .add(EMAIL_BODY_URL_PLACEHOLDER, appUrl);
        EmailTemplate emailTemplate = emailTemplateRepository.get(VERIFICATION_CODE_EMAIL_TEMPLATE_CODE);
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(verifiable.getVerificationEmail());
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }

    public MailDTO createMailDTOForPinSend(TrainingInstance ti, String email) {

        String pin = getDecryptedPin(ti.getReimbursmentPin());
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("individualName", ti.getIndividual().getFirstName())
                .add("reimbursmentPin", pin)
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("trainingStartDate", String.valueOf(ti.getTraining().getStartDate()))
                .add("trainingInstitutionName", ti.getTraining().getTrainingInstitution().getName());
        EmailTemplate emailTemplate = emailTemplateRepository.get(SEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, email, mailPlaceholders);
    }

    public MailDTO createMailDTOForPinResend(TrainingInstance ti, String email) {
        String pin = getDecryptedPin(ti.getReimbursmentPin());
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("individualName", ti.getIndividual().getFirstName())
                .add("reimbursmentPin", pin)
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("trainingStartDate", String.valueOf(ti.getTraining().getStartDate()))
                .add("trainingInstitutionName", ti.getTraining().getTrainingInstitution().getName());
        EmailTemplate emailTemplate = emailTemplateRepository.get(RESEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, email, mailPlaceholders);
    }

    private String getDecryptedPin(String reimbursmentPin) {
        if (reimbursmentPin == null) {
            return "";
        }
        return  AEScryptographer.decrypt(reimbursmentPin);
    }

    private MailDTO createAndFillMailDTO(EmailTemplate emailTemplate, String email, MailPlaceholders mailPlaceholders) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());

        return mailDTO;
    }
}
