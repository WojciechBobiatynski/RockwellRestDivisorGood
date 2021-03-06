package pl.sodexo.it.gryf.service.local.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.EmailSourceType;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.logging.NoLog;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.model.mail.EmailInstanceAttachment;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.mail.EmailType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.service.local.api.MailService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;


/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
@Service
public class MailServiceImpl implements MailService {

    //STATIC FIELDS

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    //FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailInstanceRepository emailInstanceRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    //PUBLIC METHODS

    @Override
    public MailPlaceholders createPlaceholders(){
        return new MailPlaceholders();
    }

    @Override
    public MailPlaceholders createPlaceholders(String name, String value){
        return new MailPlaceholders(name, value);
    }

    @Override
    public MailDTO createMailDTO(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesTo){
        EmailTemplate emailTemplate = emailTemplateRepository.get(emailTemplateId);
        return new MailDTO(emailTemplate.getId(),
                            addressesTo,
                            mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()),
                            mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
    }

    //METHODS - SCHEDULE MAIL

    @Override
    public MailDTO scheduleTemplateMail(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
            EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments){
        EmailTemplate emailTemplate = emailTemplateRepository.get(emailTemplateId);
        MailDTO mailDTO = new MailDTO(emailTemplate.getId(),
                                        mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()),
                                        mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()),
                                        addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                                        sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }


    @Override
    public MailDTO scheduleTemplateMail(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId,
            List<MailAttachmentDTO> attachments){
        return scheduleTemplateMail(emailTemplateId,
                mailPlaceholders,
                null, null, addressesTo, addressesCC,
                sourceType, sourceId, attachments);
    }

    @Override
    public MailDTO scheduleSystemMail(String subject, String body, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId,
            List<MailAttachmentDTO> attachments){
        MailPlaceholders stdMailPlaceholders = createPlaceholders("emailSubject", subject) .add("emailBody", body);
        EmailTemplate emailTemplate = emailTemplateRepository.get(EmailTemplate.STD_EMAIL);
        MailDTO mailDTO =  new MailDTO(emailTemplate.getId(),
                stdMailPlaceholders.replace("{$emailSubject}"),
                stdMailPlaceholders.replace("{$emailBody}"),
                addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }

    @Override
    public MailDTO scheduleSystemMail(String subject, String body, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments){
        return scheduleSystemMail(subject, body,
                                        null, null, addressesTo, addressesCC,
                                        sourceType, sourceId,
                                        attachments);
    }

    @Override
    public MailDTO scheduleMail(String emailTemplateId, String subject, String body, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC, EmailSourceType sourceType,
            Long sourceId, List<MailAttachmentDTO> attachments){
        MailDTO mailDTO = new MailDTO(emailTemplateId,
                                        subject, body,
                                        addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                                        sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }

    @Override
    public MailDTO scheduleMail(String emailTemplateId, String subject, String body, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId,
            List<MailAttachmentDTO> attachments){
        return scheduleMail(emailTemplateId,
                                subject, body,
                                null, null, addressesTo, addressesCC,
                                sourceType, sourceId,
                                attachments);
    }

    @Override
    public MailDTO scheduleMail(MailDTO mailDTO, GrantProgram grantProgram){
        EmailTemplate emailTemplate = (mailDTO.getTemplateId() != null) ? emailTemplateRepository.get(mailDTO.getTemplateId()) : null;

        if(GryfStringUtils.isEmpty(mailDTO.getAddressesFrom())){
            mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        }
        if(GryfStringUtils.isEmpty(mailDTO.getAddressesReplyTo())){
            mailDTO.setAddressesReplyTo(applicationParameters.getGryfPbeAdmEmailReplyTo());
        }

        EmailInstance em = new EmailInstance();
        em.setEmailTemplate(emailTemplate);

        em.setEmailSubject(GryfStringUtils.substring(mailDTO.getSubject(), 0, EmailInstance.EMAIL_SUBJECT_MAX_SIZE));
        em.setEmailsTo(GryfStringUtils.substring(mailDTO.getAddressesTo(), 0, EmailInstance.EMAILS_TO_MAX_SIZE));
        em.setStatus(EmailInstance.STATUS_PENDING);
        em.setEmailType((emailTemplate != null) ? emailTemplate.getEmailType() : EmailType.DEFAULT_TYPE);
        if(em.getEmailType() == EmailType.html){
            if(emailTemplate != null && !Strings.isNullOrEmpty(emailTemplate.getEmailBodyHtmlTemplate())){
                MailPlaceholders mailPlaceholders =
                        createPlaceholders("emailPlainBodyTemplates", mailDTO.getBody().replaceAll("(\r\n|\n)", "<br />"))
                                .add("emailPlainSubjectTemplates", mailDTO.getSubject());
                if (grantProgram != null){
                    mailPlaceholders.add("emailGrantProgramName", grantProgram.getProgramName());
                }
                mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyHtmlTemplate()));
            }
        }
        em.setEmailData(JsonMapperUtils.writeValueAsString(mailDTO));
        em.setErrorMessage(null);
        em.setSourceType(mailDTO.getSourceType());
        em.setSourceId(mailDTO.getSourceId());
        if(!GryfUtils.isEmpty(mailDTO.getAttachments())) {
            for (MailAttachmentDTO attachment : mailDTO.getAttachments()) {
                EmailInstanceAttachment a = new EmailInstanceAttachment();
                a.setAttachmentPath(attachment.getPath());
                a.setAttachmentName(GryfStringUtils.substring(attachment.getName(), 0, EmailInstanceAttachment.ATTACHMENT_NAME_MAX_SIZE));
                em.addAttachment(a);
            }
        }
        em.setDelayTimestamp(mailDTO.getDelayTimestamp());
        emailInstanceRepository.save(em);
        mailDTO.setEmailInstanceId(em.getId());
        return mailDTO;
    }

    @Override
    public MailDTO scheduleMail(MailDTO mailDTO){
        return scheduleMail(mailDTO, null);
    }

    //METHODS - SEND MAIL

    @Override
    @Scheduled(initialDelayString = "${gryf2.service.scheduler.mail.initialDelay:60000}", fixedDelayString = "${gryf2.service.scheduler.mail.fixedDelay:60000}")
    @NoLog
    public void sendMails(){

        //POBRANIE INSTANCJI MAILA
        List<EmailInstance> emails = emailInstanceRepository.findAvailableToSend(EmailInstance.STATUS_PENDING);
        if(!emails.isEmpty()) {
            LOGGER.info("Uruchominie zadania wysy??ajacego maile. Ilo???? maili do wys??ania jest r??wna[{}]", emails.size());
        }

        //WYSLANIE MAILI
        for (EmailInstance email : emails) {
            MailService mailService = context.getBean(MailService.class);
            mailService.sendMail(email);
        }

        if(!emails.isEmpty()) {
            LOGGER.info("Zako??czenie zadania wysy??ajacego maile");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMail(EmailInstance email){

        Session mailSession = null;

        try {
            MailDTO mailDTO = JsonMapperUtils.readValue(email.getEmailData(), MailDTO.class);

            mailSession = createDefaultMailSession();
            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(mailDTO.getAddressesFrom()));
            if(!GryfStringUtils.isEmpty(mailDTO.getAddressesReplyTo())) {
                message.setReplyTo(InternetAddress.parse(mailDTO.getAddressesReplyTo()));
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDTO.getAddressesTo()));
            if(!GryfStringUtils.isEmpty(mailDTO.getAddressesCC())) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailDTO.getAddressesCC()));
            }

            message.setSubject(mailDTO.getSubject(), "UTF-8");

            if(GryfUtils.isEmpty(mailDTO.getAttachments())){
                String type = getContentType(email.getEmailTemplate());
                message.setText(mailDTO.getBody(), "UTF-8", type);
            }
            else{
                addAttachments(message, email.getEmailTemplate(), mailDTO.getBody(), mailDTO.getAttachments());
            }

            Transport.send(message);

            fillAfterSend(email, EmailInstance.STATUS_DONE, mailSession, null);

        } catch (MessagingException | RuntimeException e) {
            fillAfterSend(email, EmailInstance.STATUS_ERROR, mailSession, e.getMessage());
            LOGGER.error("Wystapi?? b??ad przy wysy??ce maila dla id = {}", email.getId(), e);
        }

        emailInstanceRepository.update(email, email.getId());
    }

    //METHODS - OTHER

    private void addAttachments(Message message, EmailTemplate emailTemplate, String body, List<MailAttachmentDTO> attachments) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        String type = getContentType(emailTemplate);
        LOGGER.debug("Ustawiany xxx=" + emailTemplate);
        LOGGER.debug("Ustawiany typ=" + type);
        messageBodyPart.setText(body, "UTF-8", type);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        for (MailAttachmentDTO attachment : attachments) {
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment.getPath());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachment.getName());
            multipart.addBodyPart(messageBodyPart);

            //WERSJA Z INPUT STREAM - nie dzia??a, wtedy mo??na by przez fileSerwice
            //messageBodyPart = new MimeBodyPart(attachment.getInputStream());
            //messageBodyPart.setFileName(attachment.getName());
            //messageBodyPart.setHeader("Content-Type", getContentType(attachment.getName()));
            //multipart.addBodyPart(messageBodyPart);
        }
    }

    /**
     * Tworzy domy??ln?? sesj??, z podstawywmi nazwa serwera, portu itp.
     * @return nowa sesjia
     */
    private Session createDefaultMailSession(){
        Properties props = new Properties();
        props.put("mail.smtp.host", applicationParameters.getGryfPbeDefaultSmtpHost());
        props.put("mail.smtp.port", applicationParameters.getGryfPbeDefaultSmtpPort());
        return Session.getInstance(props);
    }

    private void fillAfterSend(EmailInstance email, String status, Session mailSession, String errorMessage){
        email.setStatus(status);
        email.setErrorMessage(GryfStringUtils.substring(errorMessage, 0, EmailInstance.ERROR_MESSAGE_MAX_SIZE));
        if(mailSession != null) {
            email.setMailSessionProperties(GryfStringUtils.substring(mailSession.getProperties().toString(), 0,
                    EmailInstance.MAIL_SESSION_PROPERTIES_MAX_SIZE));
        }
    }

    private String getContentType(EmailTemplate emailTemplate){
        return (emailTemplate != null && emailTemplate.getEmailType() != null) ?
                emailTemplate.getEmailType().getContentType() : EmailType.DEFAULT_TYPE.getContentType();
    }

}
