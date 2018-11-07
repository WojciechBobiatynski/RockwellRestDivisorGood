package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.common.dto.mail.EmailSourceType;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface MailService {

    MailPlaceholders createPlaceholders();

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

    MailDTO scheduleMail(MailDTO mailDTO);

    MailDTO scheduleMail(MailDTO mailDTO, GrantProgram grantProgram);

    void sendMails();

    void sendMail(EmailInstance email);
}
