package pl.sodexo.it.gryf.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.MailDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.model.mail.EmailInstanceAttachment;
import pl.sodexo.it.gryf.model.mail.EmailSourceType;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.root.repository.mail.EmailInstanceRepository;
import pl.sodexo.it.gryf.root.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.utils.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
@Service
public class MailService {

    //STATIC FIELDS

    private static final Logger LOG = Logger.getLogger(MailService.class.getName());

    //FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailInstanceRepository emailInstanceRepository;

    @Autowired
    private ApplicationParametersService applicationParametersService;

    //PUBLIC METHODS

    /**
     * Tworzy strukturę z placeholderami zasiloną pierwszym placeholderem
     * @param name  nazwa placeholdera
     * @param value wartość placeholdera
     * @return obiekt przechowujący placeholdery
     */
    public MailPlaceholders createPlaceholders(String name, String value){
        return new MailPlaceholders(name, value);
    }

    public MailDTO createMailDTO(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesTo){
        EmailTemplate emailTemplate = emailTemplateRepository.get(emailTemplateId);
        return new MailDTO(emailTemplate.getId(),
                            addressesTo,
                            mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()),
                            mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
    }

    //METHODS - SCHEDULE MAIL

    public MailDTO scheduleTemplateMail(String emailTemplateId,
                                        MailPlaceholders mailPlaceholders,
                                        String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
                                        EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments){
        EmailTemplate emailTemplate = emailTemplateRepository.get(emailTemplateId);
        MailDTO mailDTO = new MailDTO(emailTemplate.getId(),
                                        mailPlaceholders.replace(emailTemplate.getEmailSubjectTemplate()),
                                        mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()),
                                        addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                                        sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }


    public MailDTO scheduleTemplateMail(String emailTemplateId,
                                         MailPlaceholders mailPlaceholders,
                                         String addressesTo, String addressesCC,
                                         EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments){
        return scheduleTemplateMail(emailTemplateId,
                mailPlaceholders,
                null, null, addressesTo, addressesCC,
                sourceType, sourceId, attachments);
    }

    public MailDTO scheduleSystemMail(String subject, String body,
                                      String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
                                       EmailSourceType sourceType, Long sourceId,
                                       List<MailAttachmentDTO> attachments){
        MailPlaceholders stdMailPlaceholders = createPlaceholders("emailSubject", subject) .add("emailBody", body);
        EmailTemplate emailTemplate = applicationParametersService.getStdEmailTemplate();
        MailDTO mailDTO =  new MailDTO(emailTemplate.getId(),
                stdMailPlaceholders.replace(applicationParametersService.getStdEmailSubjectTemplate()),
                stdMailPlaceholders.replace(applicationParametersService.getStdEmailBodyTemplate()),
                addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }

    public MailDTO scheduleSystemMail(String subject, String body,
                                      String addressesTo, String addressesCC,
                                      EmailSourceType sourceType, Long sourceId,
                                      List<MailAttachmentDTO> attachments){
        return scheduleSystemMail(subject, body,
                                        null, null, addressesTo, addressesCC,
                                        sourceType, sourceId,
                                        attachments);
    }

    public MailDTO scheduleMail(String emailTemplateId,
                                  String subject, String body,
                                  String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
                                  EmailSourceType sourceType, Long sourceId,
                                  List<MailAttachmentDTO> attachments){
        MailDTO mailDTO = new MailDTO(emailTemplateId,
                                        subject, body,
                                        addressesFrom, addressesReplyTo, addressesTo, addressesCC,
                                        sourceType, sourceId, attachments);
        return scheduleMail(mailDTO);
    }

    public MailDTO scheduleMail(String emailTemplateId,
                                String subject, String body,
                                String addressesTo, String addressesCC,
                                EmailSourceType sourceType, Long sourceId,
                                List<MailAttachmentDTO> attachments){
        return scheduleMail(emailTemplateId,
                                subject, body,
                                null, null, addressesTo, addressesCC,
                                sourceType, sourceId,
                                attachments);
    }

    private MailDTO scheduleMail(MailDTO mailDTO){

        if(StringUtils.isEmpty(mailDTO.getAddressesFrom())){
            mailDTO.setAddressesFrom(applicationParametersService.getGryfPbeAdmEmailFrom());
        }
        if(StringUtils.isEmpty(mailDTO.getAddressesReplyTo())){
            mailDTO.setAddressesReplyTo(applicationParametersService.getGryfPbeAdmEmailReplyTo());
        }

        EmailInstance em = new EmailInstance();
        em.setEmailTemplate((mailDTO.getTemplateId() != null) ? emailTemplateRepository.get(mailDTO.getTemplateId()) : null);
        em.setEmailData(JsonMapperUtils.writeValueAsString(mailDTO));
        em.setEmailSubject(StringUtils.substring(mailDTO.getSubject(), 0, EmailInstance.EMAIL_SUBJECT_MAX_SIZE));
        em.setEmailsTo(StringUtils.substring(mailDTO.getAddressesTo(), 0, EmailInstance.EMAILS_TO_MAX_SIZE));
        em.setStatus(EmailInstance.STATUS_PENDING);
        em.setErrorMessage(null);
        em.setSourceType(mailDTO.getSourceType());
        em.setSourceId(mailDTO.getSourceId());
        if(!GryfUtils.isEmpty(mailDTO.getAttachments())) {
            for (MailAttachmentDTO attachment : mailDTO.getAttachments()) {
                EmailInstanceAttachment a = new EmailInstanceAttachment();
                a.setAttachmentPath(attachment.getPath());
                a.setAttachmentName(StringUtils.substring(attachment.getName(), 0, EmailInstanceAttachment.ATTACHMENT_NAME_MAX_SIZE));
                em.addAttachment(a);
            }
        }
        emailInstanceRepository.save(em);
        return mailDTO;
    }

    //METHODS - SEND MAIL

    @Scheduled(initialDelay = 60 * 1000, fixedDelay= 60 * 1000)
    public void sendMails(){

        //POBRANIE INSTANCJI MAILA
        List<EmailInstance> emails = emailInstanceRepository.findByStatus(EmailInstance.STATUS_PENDING);
        LOG.log(Level.INFO, String.format("Uruchominie zadania wysyłajacego maile ilość mail do wysłnia [%s]", emails.size()));

        //WYSLANIE MAILI
        for (EmailInstance email : emails) {
            MailService mailService = context.getBean(MailService.class);
            mailService.sendMail(email);
        }

        LOG.log(Level.INFO, "Zakończenie zadania wysyłajacego maile");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMail(EmailInstance email){

        Session mailSession = null;

        try {
            MailDTO mailDTO = JsonMapperUtils.readValue(email.getEmailData(), MailDTO.class);

            mailSession = createDefaultMailSession();
            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(mailDTO.getAddressesFrom()));
            if(!StringUtils.isEmpty(mailDTO.getAddressesReplyTo())) {
                message.setReplyTo(InternetAddress.parse(mailDTO.getAddressesReplyTo()));
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDTO.getAddressesTo()));
            if(!StringUtils.isEmpty(mailDTO.getAddressesCC())) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailDTO.getAddressesCC()));
            }

            message.setSubject(mailDTO.getSubject(), "UTF-8");

            if(GryfUtils.isEmpty(mailDTO.getAttachments())){
                message.setText(mailDTO.getBody(), "UTF-8");
            }
            else{
                addAttachments(message, mailDTO.getBody(), mailDTO.getAttachments());
            }

            Transport.send(message);

            fillAfterSend(email, EmailInstance.STATUS_DONE, mailSession, null);

        } catch (MessagingException | RuntimeException e) {
            fillAfterSend(email, EmailInstance.STATUS_ERROR, mailSession, e.getMessage());
            LOG.log(Level.SEVERE, "Wystapił bład przy wysyłce maila dla id = " + email.getId(), e);
        }

        emailInstanceRepository.update(email, email.getId());
    }

    //METHODS - OTHER

    private void addAttachments(Message message, String body, List<MailAttachmentDTO> attachments) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body, "UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        for (MailAttachmentDTO attachment : attachments) {
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment.getPath());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachment.getName());
            multipart.addBodyPart(messageBodyPart);

            //WERSJA Z INPUT STREAM - nie działa, wtedy można by przez fileSerwice
            //messageBodyPart = new MimeBodyPart(attachment.getInputStream());
            //messageBodyPart.setFileName(attachment.getName());
            //messageBodyPart.setHeader("Content-Type", getContentType(attachment.getName()));
            //multipart.addBodyPart(messageBodyPart);
        }
    }

    /**
     * Tworzy domyślną sesję, z podstawywmi nazwa serwera, portu itp.
     * @return nowa sesjia
     */
    private Session createDefaultMailSession(){
        Properties props = new Properties();
        props.put("mail.smtp.host", applicationParametersService.getGryfPbeDefaultSmtpHost());
        props.put("mail.smtp.port", applicationParametersService.getGryfPbeDefaultSmtpPort());
        return Session.getInstance(props);
    }

    private void fillAfterSend(EmailInstance email, String status, Session mailSession, String errorMessage){
        email.setStatus(status);
        email.setErrorMessage(StringUtils.substring(errorMessage, 0, EmailInstance.ERROR_MESSAGE_MAX_SIZE));
        if(mailSession != null) {
            email.setMailSessionProperties(StringUtils.substring(mailSession.getProperties().toString(), 0,
                    EmailInstance.MAIL_SESSION_PROPERTIES_MAX_SIZE));
        }
    }

    //CLASSES

    public class MailPlaceholders {

        //FIELDS

        private static final String PLACEHOLDER_PREFIX = "{$";
        private static final String PLACEHOLDER_SUFFIX = "}";
        private Map<String, String> mailPlaceholders;

        //CONSTRUCTORS

        private MailPlaceholders(String name, String value) {
            this.mailPlaceholders = new HashMap();
            add(name, value);
        }

        //PUBLIC METHODS

        /**
         * Dodaje placeholder do struktury
         * @param name nazwa placeholdera
         * @param value wartość placeholdera
         * @return wartosc placeholdera
         */
        public MailPlaceholders add(String name, String value) {
            mailPlaceholders.put(PLACEHOLDER_PREFIX + name + PLACEHOLDER_SUFFIX, value);
            return this;
        }

        /**
         * W zwróconym stringu zaminia wystapienia według wartosci klucz - wartosc dodanych do placeholdera.
         * @param val wartosc zaminiana
         * @return zamianione wartosci
         */
        public String replace(String val){
            for (String key : mailPlaceholders.keySet()) {
                val = val.replace(key, mailPlaceholders.get(key));
            }
            return val;
        }
    }
    
}
