package pl.sodexo.it.gryf.service.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
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

    public MailDTO createMailDTOForVerificationCode(String email, String verificationParam) {
        MailDTO mailDTO = new MailDTO();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders(EMAIL_BODY_VER_CODE_PLACEHOLDER, verificationParam);
        EmailTemplate emailTemplate = emailTemplateRepository.get(VERIFICATION_CODE_EMAIL_TEMPLATE_CODE);
        mailDTO.setTemplateId(emailTemplate.getId());
        mailDTO.setSubject(emailTemplate.getEmailSubjectTemplate());
        mailDTO.setAddressesFrom(applicationParameters.getGryfPbeAdmEmailFrom());
        mailDTO.setAddressesTo(email);
        mailDTO.setBody(mailPlaceholders.replace(emailTemplate.getEmailBodyTemplate()));
        mailDTO.setAttachments(Collections.emptyList());
        return mailDTO;
    }


}
