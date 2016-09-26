package pl.sodexo.it.gryf.service.api.other;

import pl.sodexo.it.gryf.model.mail.EmailTemplate;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ApplicationParametersService {

    String getCdnUrl();

    String getResourcesUrl();

    int getSessionTimeout();

    String getJsUrl();

    String getTemplatesUrl();

    String getPathAttachments();

    String getPathGrantApp();

    String getPathOrders();

    String getPathReportTemplates();

    String getPathReimbursements();

    int getAttachmentMaxSize();

    int getAttachmentMaxSizeMB();

    String getGryfEnterpriseCodePrefix();

    String getGryfIndividualCodePrefix();

    int getGryfEnterpriseCodeZeroCount();

    int getGryfIndividualCodeZeroCount();

    String getGryfTrainingInstitutionCodePrefix();

    int getGryfTrainingInstitutionCodeZeroCount();

    EmailTemplate getStdEmailTemplate();

    String getStdEmailBodyTemplate();

    String getStdEmailSubjectTemplate();

    String getGryfPbeDefaultSmtpHost();

    String getGryfPbeDefaultSmtpPort();

    String getGryfPbeAdmEmailFrom();

    String getGryfPbeAdmEmailReplyTo();

    String getGryfPbeDefPubEmailFrom();

    String getGryfPbeDefPubEmailReplyTo();

    String getSodexoName();

    String getSodexoAddress1();

    String getSodexoAddress2();

    String getSodexoVatRegNum();

    String getPathReportImages();
}
