package pl.sodexo.it.gryf.common.config;

import java.util.Set;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ApplicationParameters {

    String getParametersInfo();

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

    String getPathDataImport();

    String getPathAccountingDocument();

    String getPathEreimbursements();

    String getPathAccountingDocumentArchive();

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

    String getSodexoBankName();

    String getDocumentGeneratePlace();

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

    String getTiUserContext();

    String getIndUserContext();

    String getIndUserUrl();

    String getTiUserUrl();

    Integer getBusinessDaysNumberForReimbursement();

    Integer getBusinessDaysNumberForCorrection();

    String getErmbsEmailAttachmentDirectory();

    Integer getDefaultEReimburseDayLimit();

    Integer getDefaultDaysNumberAfterEndDateToExpiryPool();

    Set<String> getEreimbursmentAttachmentFileExtensionSet();

    Set<String> getEreimbursmentAttachmentContentTypeSet();

    String getStrongPasswordRegexp();

    String getExternalOrderIdPatternRegexp();

    String getCdnTitle();

    String getImportTraningSearchPattern();

    String findParameterValueByCode(String toString);
}
