package pl.sodexo.it.gryf.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;

import javax.annotation.PostConstruct;

/**
 * Konfiguracja szablonów mailowych
 *
 * Created by jbentyn on 2016-09-28.
 */
@Component
//TODO przerobić na cache
public class MailTemplateConfiguration {

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Getter
    private EmailTemplate stdEmailTemplate = null;

    @Getter
    private String stdEmailBodyTemplate = "{$emailBody}";

    @Getter
    private String stdEmailSubjectTemplate = "{$emailSubject}";

    @PostConstruct
    private void init() {
        EmailTemplate dbStdEmailTemplate = emailTemplateRepository.get(EmailTemplate.STD_EMAIL);
        if (dbStdEmailTemplate != null) {
            stdEmailTemplate = dbStdEmailTemplate;
            stdEmailSubjectTemplate = dbStdEmailTemplate.getEmailSubjectTemplate();
            stdEmailBodyTemplate = dbStdEmailTemplate.getEmailBodyTemplate();
        }
    }
}
