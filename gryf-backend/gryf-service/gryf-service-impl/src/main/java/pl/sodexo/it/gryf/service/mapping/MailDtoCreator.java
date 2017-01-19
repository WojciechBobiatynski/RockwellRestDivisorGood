package pl.sodexo.it.gryf.service.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionNotificationEmailParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailParamsDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.Verifiable;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.local.api.MailService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public MailDTO createMailDTOForResetLink(String email, String newLink) {
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_RESET_LINK_PLACEHOLDER, applicationParameters.getTiUserUrl() + RESET_LINK_URL_PREFIX + newLink);
        EmailTemplate emailTemplate = emailTemplateRepository.get(RESET_LINK_EMAIL_TEMPLATE_CODE);
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }

    public MailDTO createMailDTOForVerificationCode(Verifiable verifiable, String firstName, String lastName) {
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(FIRST_NAME_PLACEHOLDER, firstName)
                .add(LAST_NAME_PLACEHOLDER, lastName)
                .add(EMAIL_BODY_VER_CODE_PLACEHOLDER, verifiable.getVerificationCode())
                .add(EMAIL_BODY_LOGIN_PLACEHOLDER, verifiable.getLogin())
                .add(EMAIL_BODY_URL_PLACEHOLDER, applicationParameters.getIndUserUrl());
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
                .add("grantProgramName", ti.getGrantProgram().getProgramName())
                .add("firstName", ti.getIndividual().getFirstName())
                .add("lastName", ti.getIndividual().getLastName())
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("trainingInstitutionName", ti.getTraining().getTrainingInstitution().getName())
                .add("trainingStartDate", new SimpleDateFormat(DATE_FORMAT).format(ti.getTraining().getStartDate()))
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("reimbursmentPin", pin);
        EmailTemplate emailTemplate = emailTemplateRepository.get(SEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, email, mailPlaceholders);
    }

    public MailDTO createMailDTOForPinResend(TrainingInstance ti, String email) {
        String pin = getDecryptedPin(ti.getReimbursmentPin());
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", ti.getGrantProgram().getProgramName())
                .add("individualName", ti.getIndividual().getFirstName())
                .add("reimbursmentPin", pin)
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("trainingStartDate", new SimpleDateFormat(DATE_FORMAT).format(ti.getTraining().getStartDate()))
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
        mailDTO.setSubject(mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()));
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeDefPubEmailFrom());
        mailDTO.setAddressesReplyTo(applicationParameters.getGryfPbeDefPubEmailReplyTo());
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());

        return mailDTO;
    }

    public List<MailDTO> createMailDTOsForCorrecionNotification(CorrectionNotificationEmailParamsDto paramsDto) {
        List<MailDTO> result = new ArrayList<>();
        paramsDto.getEmails().stream().forEach(email -> {
            MailDTO mailDTO = new MailDTO();
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            MailPlaceholders mailPlaceholders = mailService.createPlaceholders(ARRIVAL_DATE_PLACEHOLDER, format.format(paramsDto.getArrivalDate()))
                    .add(RMBS_NUMBER_PLACEHOLDER, paramsDto.getRmbsNumber().toString())
                    .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName());
            EmailTemplate emailTemplate = emailTemplateRepository.get(CORR_NOTIF_EMAIL_TEMPLATE_CODE);
            mailDTO.setTemplateId(emailTemplate.getId());
            mailDTO.setSubject(mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()));
            mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
            mailDTO.setAddressesTo(email);
            mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
            mailDTO.setAttachments(Collections.emptyList());
            result.add(mailDTO);
        });
        return result;
    }

    public MailDTO createConfirmReimbMailDto(ErmbsMailParamsDto paramsDto) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(TRAINING_NAME_PLACEHOLDER, paramsDto.getTrainingName())
                .add(NOTE_NOT_PLACEHOLDER, paramsDto.getNoteNo());
        EmailTemplate emailTemplate = emailTemplateRepository.get(E_REIMB_CONFIRMATION_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, paramsDto.getEmail(), mailPlaceholders);
    }

    public MailDTO createConfirmPaymentMailDto(ErmbsMailParamsDto paramsDto) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(TRAINING_NAME_PLACEHOLDER, paramsDto.getTrainingName());
        String emailTemplateCode = CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE;
        if(paramsDto.isContractForEnterprise()){
            emailTemplateCode = CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE_FOR_ENTERPRISE;
        }
        EmailTemplate emailTemplate = emailTemplateRepository.get(emailTemplateCode);
        return createAndFillMailDTO(emailTemplate, paramsDto.getEmail(), mailPlaceholders);
    }

    public MailDTO createMailDTOForEreimbMail(ErmbsMailDto dto){
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_PLACEHOLDER, dto.getEmailBody())
                .add(EMAIL_SUBJECT_PLACEHOLDER, dto.getEmailSubject());
        EmailTemplate emailTemplate = emailTemplateRepository.get(DEFAULT_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, dto.getEmailsTo(), mailPlaceholders);
    }
}
