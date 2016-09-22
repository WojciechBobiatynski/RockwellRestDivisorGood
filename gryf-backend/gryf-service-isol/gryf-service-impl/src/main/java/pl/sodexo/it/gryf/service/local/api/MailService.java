package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.common.dto.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.model.mail.EmailSourceType;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface MailService {

    /**
     * Tworzy strukturę z placeholderami zasiloną pierwszym placeholderem
     * @param name  nazwa placeholdera
     * @param value wartość placeholdera
     * @return obiekt przechowujący placeholdery
     */
    MailPlaceholders createPlaceholders(String name, String value);

    MailDTO createMailDTO(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesTo);

    MailDTO scheduleTemplateMail(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
            EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments);

    MailDTO scheduleTemplateMail(String emailTemplateId, MailPlaceholders mailPlaceholders, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId,
            List<MailAttachmentDTO> attachments);

    MailDTO scheduleSystemMail(String subject, String body, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId,
            List<MailAttachmentDTO> attachments);

    MailDTO scheduleSystemMail(String subject, String body, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments);

    MailDTO scheduleMail(String emailTemplateId, String subject, String body, String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC, EmailSourceType sourceType,
            Long sourceId, List<MailAttachmentDTO> attachments);

    MailDTO scheduleMail(String emailTemplateId, String subject, String body, String addressesTo, String addressesCC, EmailSourceType sourceType, Long sourceId, List<MailAttachmentDTO> attachments);

    void sendMails();

    void sendMail(EmailInstance email);
}
