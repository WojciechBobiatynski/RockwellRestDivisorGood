package pl.sodexo.it.gryf.service.local.impl.publicbenefits.grantapplications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.*;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationEmailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class GrantApplicationEmailServiceImpl implements GrantApplicationEmailService {

    @Autowired
    private ApplicationContext context;

    @Override
    public void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId, MailPlaceholders mailPlaceholders, String emailsTo, List<MailAttachmentDTO> attachments) {
        GrantApplicationVersion applicationVersion = grantApplication.getApplicationVersion();
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        grantApplicationService.sendPublicGrantProgramEmail(grantApplication, emailTemplateId, mailPlaceholders, emailsTo, attachments);
    }

    @Override
    public void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId, String subject, String body, String emailsTo, List<MailAttachmentDTO> attachments) {
        GrantApplicationVersion applicationVersion = grantApplication.getApplicationVersion();
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        grantApplicationService.sendPublicGrantProgramEmail(grantApplication, emailTemplateId, subject, body, emailsTo, attachments);
    }

    @Override
    public Set<String> getEmailRecipients(GrantApplication grantApplication, Set<String> existingRecipientsSet) {
        Set<String> set;
        if (existingRecipientsSet == null) {
            set = new HashSet<>();
        } else {
            set = existingRecipientsSet;
        }
        if (grantApplication != null) {
            GrantApplicationBasicData basicData = grantApplication.getBasicData();
            for (GrantApplicationContactData contact : basicData.getContacts()) {
                if (contact.getContactType() == GrantApplicationContactType.CONTACT) {
                    if (!GryfStringUtils.isEmpty(contact.getEmail())) {
                        set.add(contact.getEmail());
                    }
                }
            }
        }
        return set;
    }

}
