package pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-16.
 */
public interface GrantApplicationService<T extends GrantApplicationDTO> {

    String findActualApplicationFormData(GrantApplication application);

    GrantApplication save(T dto);

    GrantApplication update(T dto);

    GrantApplication apply(T dto, List<String> acceptedViolationList);

    GrantApplication execute(Long id, T dto, boolean checkVatRegNumDup);

    GrantApplication reject(Long id,  T dto);
    
    void sendPublicGrantProgramEmail(GrantApplication application, String emailTemplateId,
                                      MailPlaceholders mailPlaceholders, String emailsTo, List<MailAttachmentDTO> attachments);

    void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId,
                                     String subject, String body, String emailsTo, List<MailAttachmentDTO> attachments);
}
