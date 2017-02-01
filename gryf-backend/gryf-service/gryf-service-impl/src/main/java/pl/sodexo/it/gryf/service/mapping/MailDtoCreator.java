package pl.sodexo.it.gryf.service.mapping;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionNotificationEmailParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsGrantProgramParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailParamsDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.Verifiable;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwner;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    @Autowired
    private ParamInDateService paramInDateService;

    //PUBLIC METHODS - LOGIN MAILS

    public MailDTO createMailDTOForResetLink(String email, String newLink) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(EMAIL_BODY_RESET_LINK_PLACEHOLDER, applicationParameters.getTiUserUrl() + RESET_LINK_URL_PREFIX + newLink);

        EmailTemplate emailTemplate = emailTemplateRepository.get(RESET_LINK_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders, email);
    }

    public MailDTO createMailDTOForVerificationCode(Verifiable verifiable, String firstName, String lastName) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(FIRST_NAME_PLACEHOLDER, firstName)
                .add(LAST_NAME_PLACEHOLDER, lastName)
                .add(EMAIL_BODY_VER_CODE_PLACEHOLDER, verifiable.getVerificationCode())
                .add(EMAIL_BODY_LOGIN_PLACEHOLDER, verifiable.getLogin())
                .add(EMAIL_BODY_URL_PLACEHOLDER, applicationParameters.getIndUserUrl());

        EmailTemplate emailTemplate = emailTemplateRepository.get(VERIFICATION_CODE_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders, verifiable.getVerificationEmail());
    }

    public MailDTO createMailDTOForTiAccess(GrantProgram grantProgram, GryfTiUserDto user, String newLink) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add("grantProgramName", grantProgram.getProgramName())
                .add("IndividualWebAppURL",  applicationParameters.getTiUserUrl())
                .add("login", user.getLogin())
                .add("resetLink", applicationParameters.getTiUserUrl() + RESET_LINK_URL_PREFIX + newLink);

        MailGrantProgramParam mailGpp = createGrantProgramParam(grantProgram);
        EmailTemplate emailTemplate = emailTemplateRepository.get(TI_ACCESS_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                user.getEmail(), null, mailGpp.getEmailFrom(), mailGpp.getEmailReplay());
    }

    //PUBLIC METHODS - TRAINING INSTANCES MAILS

    public MailDTO createMailDTOForPinSend(TrainingInstance ti, String email) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add("individualName", ti.getIndividual().getFirstName())
                .add("grantProgramName", ti.getGrantProgram().getProgramName())
                .add("firstName", ti.getIndividual().getFirstName())
                .add("lastName", ti.getIndividual().getLastName())
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("trainingInstitutionName", ti.getTraining().getTrainingInstitution().getName())
                .add("trainingStartDate", new SimpleDateFormat(DATE_FORMAT).format(ti.getTraining().getStartDate()))
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("reimbursmentPin", getDecrypt(ti.getReimbursmentPin()));

        MailGrantProgramParam mailGpp = createGrantProgramParam(ti.getGrantProgram());
        EmailTemplate emailTemplate = emailTemplateRepository.get(SEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                email, null, mailGpp.getEmailFrom(), mailGpp.getEmailReplay());
    }

    public MailDTO createMailDTOForPinResend(TrainingInstance ti, String email) {

        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add("grantProgramName", ti.getGrantProgram().getProgramName())
                .add("individualName", ti.getIndividual().getFirstName())
                .add("reimbursmentPin", getDecrypt(ti.getReimbursmentPin()))
                .add("trainingName", ti.getTraining().getName())
                .add("trainingPlace", ti.getTraining().getPlace())
                .add("assignedProductNum", String.valueOf(ti.getAssignedNum()))
                .add("trainingStartDate", new SimpleDateFormat(DATE_FORMAT).format(ti.getTraining().getStartDate()))
                .add("trainingInstitutionName", ti.getTraining().getTrainingInstitution().getName());

        MailGrantProgramParam mailGpp = createGrantProgramParam(ti.getGrantProgram());
        EmailTemplate emailTemplate = emailTemplateRepository.get(RESEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                email, null, mailGpp.getEmailFrom(), mailGpp.getEmailReplay());
    }

    //PUBLIC METHODS - REIMBURSMENT MAILS

    public MailDTO createExpireReimbursmentMailDto(ErmbsMailParamsDto paramsDto, ErmbsGrantProgramParamsDto grantProgramParam) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(NOTE_NOT_PLACEHOLDER, paramsDto.getNoteNo());

        EmailTemplate emailTemplate = emailTemplateRepository.get(EXPI_REIMB_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                paramsDto.getEmail(), grantProgramParam.getGrantProgramEmailCC(),
                grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay());
    }

    public MailDTO createCreditNoteForUnreservedPoolMailDto(ErmbsMailParamsDto paramsDto, ErmbsGrantProgramParamsDto grantProgramParam) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(NOTE_NOT_PLACEHOLDER, paramsDto.getNoteNo());

        EmailTemplate emailTemplate = getPoolReimbEmailTemplate(paramsDto);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                paramsDto.getEmail(), grantProgramParam.getGrantProgramEmailCC(),
                grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay());
    }

    private EmailTemplate getPoolReimbEmailTemplate(ErmbsMailParamsDto paramsDto) {
        EmailTemplate emailTemplate;
        if(EreimbursementType.RET_POOL.equals(paramsDto.getErmbsTypeId())){
            emailTemplate = emailTemplateRepository.get(E_REIMB_CONFIRMATION_EMAIL_TEMPLATE_CODE_FOR_UNRSV_POOL);
        } else {
            emailTemplate = emailTemplateRepository.get(EXPI_REIMB_EMAIL_TEMPLATE_CODE);
        }
        return emailTemplate;
    }

    public MailDTO createCreditNoteMailDto(ErmbsMailParamsDto paramsDto, ErmbsGrantProgramParamsDto grantProgramParam) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(TRAINING_NAME_PLACEHOLDER, paramsDto.getTrainingName())
                .add(NOTE_NOT_PLACEHOLDER, paramsDto.getNoteNo());

        EmailTemplate emailTemplate = emailTemplateRepository.get(E_REIMB_CONFIRMATION_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                paramsDto.getEmail(), null,
                grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay());
    }

    public MailDTO createConfirmPaymentMailDto(ErmbsMailParamsDto paramsDto, ErmbsGrantProgramParamsDto grantProgramParam) {
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName())
                .add(FIRST_NAME_PLACEHOLDER, paramsDto.getFirstName())
                .add(LAST_NAME_PLACEHOLDER, paramsDto.getLastName())
                .add(TRAINING_NAME_PLACEHOLDER, paramsDto.getTrainingName());

        EmailTemplate emailTemplate = emailTemplateRepository.get(paramsDto.isContractForEnterprise() ?
                                                            CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE_FOR_ENTERPRISE :
                                                            CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                    paramsDto.getEmail(), null,
                grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay());
    }

    public MailDTO createMailDTOForEreimbMail(ErmbsMailDto dto, ErmbsGrantProgramParamsDto grantProgramParam){
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                .add(EMAIL_BODY_PLACEHOLDER, dto.getEmailBody())
                .add(EMAIL_SUBJECT_PLACEHOLDER, dto.getEmailSubject());

        EmailTemplate emailTemplate = emailTemplateRepository.get(DEFAULT_HTML_EMAIL_TEMPLATE_CODE);
        return createAndFillMailDTO(emailTemplate, mailPlaceholders,
                dto.getEmailsTo(), null,
                grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay());
    }

    public List<MailDTO> createMailDTOsForCorrecionNotification(CorrectionNotificationEmailParamsDto paramsDto, ErmbsGrantProgramParamsDto grantProgramParam) {
        List<MailDTO> result = new ArrayList<>();
        paramsDto.getEmails().stream().forEach(email -> {

            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            MailPlaceholders mailPlaceholders = mailService.createPlaceholders()
                    .add(ARRIVAL_DATE_PLACEHOLDER, format.format(paramsDto.getArrivalDate()))
                    .add(RMBS_NUMBER_PLACEHOLDER, paramsDto.getRmbsNumber().toString())
                    .add(GRANT_PROGRAM_PLACEHOLDER, paramsDto.getGrantProgramName());

            EmailTemplate emailTemplate = emailTemplateRepository.get(CORR_NOTIF_EMAIL_TEMPLATE_CODE);
            result.add(createAndFillMailDTO(emailTemplate, mailPlaceholders,
                    email, null,
                    grantProgramParam.getGrantProgramEmailFrom(), grantProgramParam.getGrantProgramEmailReplay()));
        });
        return result;
    }

    //PRIVATE METHODS - CREATE MAILS

    private MailDTO createAndFillMailDTO(EmailTemplate emailTemplate, MailPlaceholders mailPlaceholders, String email,
                                        String emailCC, String emailFrom, String emialReplayTo) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()));
        mailDTO.setAddressesCC(emailCC);
        mailDTO.setAddressesFrom(Strings.isNullOrEmpty(emailFrom) ? applicationParameters.getGryfPbeDefPubEmailFrom() : emailFrom);
        mailDTO.setAddressesReplyTo(Strings.isNullOrEmpty(emialReplayTo) ? applicationParameters.getGryfPbeDefPubEmailReplyTo() : emialReplayTo);
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }

    private MailDTO createAndFillMailDTO(EmailTemplate emailTemplate, MailPlaceholders mailPlaceholders, String email) {
        return createAndFillMailDTO(emailTemplate, mailPlaceholders, email, null, null, null);
    }

    //PRIVATE METHODS - OTHERS

    private String getDecrypt(String val) {
        return (val != null) ? AEScryptographer.decrypt(val) : "";
    }

    //PRIVATE METHODS - GRANT PROGRAM PARAM

    private MailGrantProgramParam createGrantProgramParam(GrantProgram grantProgram){
        GrantOwner grantOwner = grantProgram.getGrantOwner();
        GrantProgramParam gppEmialFrom = paramInDateService.findGrantProgramParam(grantProgram.getId(), GrantProgramParam.EMAIL_FROM, new Date(), false);
        GrantProgramParam gppEmialReplayTo = paramInDateService.findGrantProgramParam(grantProgram.getId(), GrantProgramParam.EMAIL_REPLAY_TO, new Date(), false);

        MailGrantProgramParam dto = new MailGrantProgramParam();
        dto.setEmailCC(grantOwner.getEmailAddressesGrantAppInfo());
        dto.setEmailFrom(gppEmialFrom != null ? gppEmialFrom.getValue() : null);
        dto.setEmailReplay(gppEmialReplayTo != null ? gppEmialReplayTo.getValue() : null);
        return dto;
    }

    private class MailGrantProgramParam{

        @Getter
        @Setter
        private String emailCC;

        @Getter
        @Setter
        private String emailFrom;

        @Getter
        @Setter
        private String emailReplay;

    }
}
