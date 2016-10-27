package pl.sodexo.it.gryf.common.config;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ApplicationParameters {

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

    String getPrintNumberCountryCodePoland();

    Integer getVerificationCodeLength();

    Integer getMaxLoginFailureAttempts();

    Integer getUserLoginBlockMinutes();

    String getVerEmailContactType();

    Integer getMaxIndResetFailureAttempts();

    Integer getIndUserResetBlockMinutes();

    Integer getResetLinkActiveMinutes();

    String getPublicCaptchaKey();

    String getSecretCaptchaKey();
}
