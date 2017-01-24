package pl.sodexo.it.gryf.service.config;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Marcel.GOLUNSKI
 */
@Component
public class ApplicationParametersImpl implements ApplicationParameters {

    @PersistenceContext
    private EntityManager em;

    private String cdnUrl = "//cdn.sodexo.pl/gryf";
    private String resourcesUrl = "/resources/";
    private int sessionTimeout = 30;
    private String jsUrl = "/js/";
    private String templatesUrl = "/templates/";
    private String pathAttachments = "\\\\ceplsvc-wawdb20\\gryf\\";
    private String pathGrantApp = "grant_applications\\dev\\";
    private String pathOrders = "orders\\dev\\";
    private String pathReportTemplates = "report_templates/";
    private String pathReimbursements = "reimbursements\\dev\\";
    private String pathDataImport = "import\\";
    private String pathAccountingDocument = "accounting_documents\\dev\\";
    private String pathEreimbursements = "ereimbursements\\dev\\";
    private int attachmentMaxSize = 3145728;
    private String gryfEnterpriseCodePrefix = "6";
    private int gryfEnterpriseCodeZeroCount = 7;
    private String gryfIndividualCodePrefix = "6";
    private int gryfIndividualCodeZeroCount = 7;
    private String gryfTrainingInstitutionCodePrefix = "8";
    private int gryfTrainingInstitutionCodeZeroCount = 7;
    private String gryfPbeDefaultSmtpHost = "plsrv-5e.sodexhopass.polska";
    private String gryfPbeDefaultSmtpPort = "25";
    private String gryfPbeAdmEmailFrom = "system.bonow.szkoleniowych@bony.sodexo.pl";
    private String gryfPbeAdmEmailReplyTo = "tbok.kk@sodexo.com";
    private String gryfPbeDefPubEmailFrom = "system.bonow.szkoleniowych@bony.sodexo.pl";
    private String gryfPbeDefPubEmailReplyTo = "tbok.kk@sodexo.com";
    private String sodexoName = "Sodexo Benefits and Rewards Services Polska Sp. z o.o.";
    private String sodexoAddress1 = "ul. Kłobucka 25";
    private String sodexoAddress2 = "02-699 Warszawa";
    private String sodexoVatRegNum = "5222357343";
    private String sodexoBankName = "Bank BGŻ BNP Paribas S.A.";
    private String documentGeneratePlace = "Warszawa";
    private String pathReportImages = "report_images/";
    private String printNumberCountryCodePoland = "31";
    private Integer verificationCodeLength = 8;
    private Integer maxLoginFailureAttempts = 5;
    private Integer userLoginBlockMinutes = 5;
    private String verEmailContactType = "VER_EMAIL";
    private Integer maxIndResetFailureAttempts = 5;
    private Integer indUserResetBlockMinutes = 5;
    private Integer resetLinkActiveMinutes = 30;
    private String secretCaptchaKey = "6LeC_BIUAAAAAHGZ8nD7_1DjFlTxZZIoK-38LigK";
    private String publicCaptchaKey = "6LeC_BIUAAAAAA5tGG8XLtf4pnqlbQ3PPL6fJV8E";
    private String indUserContext = "IND";
    private String tiUserContext = "TI";
    private String indUserUrl = "http://localhost:8080/gryf-ind/";
    private String tiUserUrl = "http://localhost:8080/gryf-ti/";
    private Integer businessDaysNumberForReimbursement = 5;
    private Integer businessDaysNumberForCorrection = 5;
    private String ermbsEmailAttachmentDirectory = "mail_attachments";

    //LIFECYCLE METHODS

    @PostConstruct
    private void queryParameters() {
        String dbCdnUrl = (String) findParameter("GRYF_CDN_URL");
        if (dbCdnUrl != null) {
            cdnUrl = dbCdnUrl;
        }
        String dbResourcesUrl = (String) findParameter("GRYF_RESOURCES_URL");
        if (dbResourcesUrl != null) {
            resourcesUrl = dbResourcesUrl;
        }
        String dbSessionTimeout = (String) findParameter("GRYF_WEB_SESSION_TIMEOUT");
        if (dbSessionTimeout != null) {
            sessionTimeout = Integer.valueOf(dbSessionTimeout);
        }
        String dbJsUrl = (String) findParameter("GRYF_JS_URL");
        if (dbJsUrl != null) {
            jsUrl = dbJsUrl;
        }
        String dbTemplatesUrl = (String) findParameter("GRYF_TEMPLATES_URL");
        if (dbTemplatesUrl != null) {
            templatesUrl = dbTemplatesUrl;
        }
        String dbPathAttachments = (String) findParameter("GRYF_PATH_ATTACHMENTS");
        if (dbPathAttachments != null) {
            pathAttachments = dbPathAttachments;
        }
        String dbPathGrantApp = (String) findParameter("GRYF_PATH_GRANT_APP");
        if (dbPathGrantApp != null) {
            pathGrantApp = dbPathGrantApp;
        }
        String dbPathOrders = (String) findParameter("GRYF_PATH_ORDERS");
        if (dbPathOrders != null) {
            pathOrders = dbPathOrders;
        }
        String dbPathReportTemplates = (String) findParameter("GRYF_PATH_REPORT_TEMPLATES");
        if (dbPathReportTemplates != null) {
            pathReportTemplates = dbPathReportTemplates;
        }
        String dbPathReimbursements = (String) findParameter("GRYF_PATH_REIMBURSEMENTS");
        if (dbPathReimbursements != null) {
            pathReimbursements = dbPathReimbursements;
        }
        String dbAttachmentMaxSize = (String) findParameter("GRYF_ATTACHMENT_MAX_SIZE");
        if (dbAttachmentMaxSize != null) {
            attachmentMaxSize = Integer.valueOf(dbAttachmentMaxSize);
        }
        String dbGryfEnterpriseCodePrefix = (String) findParameter("GRYF_ENTERPRISE_CODE_PREFIX");
        if (dbGryfEnterpriseCodePrefix != null) {
            gryfEnterpriseCodePrefix = dbGryfEnterpriseCodePrefix;
        }
        String dbGryfIndividualCodePrefix = (String) findParameter("GRYF_INDIVIDUAL_CODE_PREFIX");
        if (dbGryfIndividualCodePrefix != null) {
            gryfIndividualCodePrefix = dbGryfIndividualCodePrefix;
        }
        String dbGryfEnterpriseCodeZeroCount = (String) findParameter("GRYF_ENTERPRISE_CODE_ZERO_COUNT");
        if (dbGryfEnterpriseCodeZeroCount != null) {
            gryfEnterpriseCodeZeroCount = Integer.valueOf(dbGryfEnterpriseCodeZeroCount);
        }
        String dbGryfIndividualCodeZeroCount = (String) findParameter("GRYF_INDIVIDUAL_CODE_ZERO_COUNT");
        if (dbGryfIndividualCodeZeroCount != null) {
            gryfIndividualCodeZeroCount = Integer.valueOf(dbGryfIndividualCodeZeroCount);
        }
        String dbGryfTrainingInstitutionCodePrefix = (String) findParameter("GRYF_TR_INSTITUTION_CODE_PREFIX");
        if (dbGryfTrainingInstitutionCodePrefix != null) {
            gryfTrainingInstitutionCodePrefix = dbGryfTrainingInstitutionCodePrefix;
        }
        String dbGryfTrainingInstitutionCodeZeroCount = (String) findParameter("GRYF_TR_INSTITUTION_CODE_ZERO_COUNT");
        if (dbGryfTrainingInstitutionCodeZeroCount != null) {
            gryfTrainingInstitutionCodeZeroCount = Integer.valueOf(dbGryfTrainingInstitutionCodeZeroCount);
        }
        String dbGryfPbeDefaultSmtpHost = (String) findParameter("GRYF_PBE_DEFAULT_SMTP_HOST");
        if (dbGryfPbeDefaultSmtpHost != null) {
            gryfPbeDefaultSmtpHost = dbGryfPbeDefaultSmtpHost;
        }
        String dbGryfPbeDefaultSmtpPort = (String) findParameter("GRYF_PBE_DEFAULT_SMTP_PORT");
        if (dbGryfPbeDefaultSmtpPort != null) {
            gryfPbeDefaultSmtpPort = dbGryfPbeDefaultSmtpPort;
        }
        String dbGryfPbeAdmEmailFrom = (String) findParameter("GRYF_PBE_ADM_EMAIL_FROM");
        if (dbGryfPbeAdmEmailFrom != null) {
            gryfPbeAdmEmailFrom = dbGryfPbeAdmEmailFrom;
        }
        String dbGryfPbeAdmEmailReplyTo = (String) findParameter("GRYF_PBE_ADM_EMAIL_REPLY_TO");
        if (dbGryfPbeAdmEmailReplyTo != null) {
            gryfPbeAdmEmailReplyTo = dbGryfPbeAdmEmailReplyTo;
        }
        String dbGryfPbeDefPubEmailFrom = (String) findParameter("GRYF_PBE_DEF_PUB_EMAIL_FROM");
        if (dbGryfPbeDefPubEmailFrom != null) {
            gryfPbeDefPubEmailFrom = dbGryfPbeDefPubEmailFrom;
        }
        String dbGryfPbeDefPubEmailReplyTo = (String) findParameter("GRYF_PBE_DEF_PUB_EMAIL_REPLYTO");
        if (dbGryfPbeDefPubEmailReplyTo != null) {
            gryfPbeDefPubEmailReplyTo = dbGryfPbeDefPubEmailReplyTo;
        }
        String dbSodexoName = (String) findParameter("GRYF_DOCUMENT_GENERATE_PLACE");
        if (dbSodexoName != null) {
            sodexoName = dbSodexoName;
        }
        String dbSodexoAddress1 = (String) findParameter("GRYF_SODEXO_ADDRESS_1");
        if (dbSodexoAddress1 != null) {
            sodexoAddress1 = dbSodexoAddress1;
        }
        String dbSodexoAddress2 = (String) findParameter("GRYF_SODEXO_ADDRESS_2");
        if (dbSodexoAddress2 != null) {
            sodexoAddress2 = dbSodexoAddress2;
        }
        String dbSodexoVatRegNum = (String) findParameter("GRYF_SODEXO_VAT_REG_NUM");
        if (dbSodexoVatRegNum != null) {
            sodexoVatRegNum = dbSodexoVatRegNum;
        }
        String dbSodexoBankName = (String) findParameter("GRYF_SODEXO_BANK_NAME");
        if (dbSodexoBankName != null) {
            sodexoBankName = dbSodexoBankName;
        }
        String dbDocumentGeneratePlace = (String) findParameter("GRYF_DOCUMENT_GENERATE_PLACE");
        if (dbDocumentGeneratePlace != null) {
            documentGeneratePlace = dbDocumentGeneratePlace;
        }
        String dbPathReportImages = (String) findParameter("GRYF_PATH_REPORT_IMAGES");
        if (dbPathReportImages != null) {
            pathReportImages = dbPathReportImages;
        }
        String dbPrintNumberCountryCodePoland = (String) findParameter("GRYF_PRINT_NUM_CODE_PL");
        if (dbPrintNumberCountryCodePoland != null) {
            printNumberCountryCodePoland = dbPrintNumberCountryCodePoland;
        }
        Integer dbVerificationCodeLength = (Integer) findParameter("GRYF_VER_CODE_LENGTH");
        if (dbVerificationCodeLength != null) {
            verificationCodeLength = dbVerificationCodeLength;
        }
        Integer dbMaxLoginFailureAttempts = (Integer) findParameter("GRYF_MAX_LOGIN_FAILURE_ATTEMPTS");
        if (dbMaxLoginFailureAttempts != null) {
            maxLoginFailureAttempts = dbMaxLoginFailureAttempts;
        }
        Integer dbUserLoginBlockMinutes = (Integer) findParameter("GRYF_USER_LOGIN_BLOCK_MINUTES");
        if (dbUserLoginBlockMinutes != null) {
            userLoginBlockMinutes = dbUserLoginBlockMinutes;
        }
        String dbVerEmailContactType = (String) findParameter("GRYF_VER_EMAIL_CONTACT_TYPE");
        if (dbVerEmailContactType != null) {
            verEmailContactType = dbVerEmailContactType;
        }
        Integer dbMaxIndResetFailureAttempts = (Integer) findParameter("GRYF_MAX_IND_RESET_FAILURE_ATTEMPTS");
        if (dbMaxIndResetFailureAttempts != null) {
            maxIndResetFailureAttempts = dbMaxIndResetFailureAttempts;
        }
        Integer dbIndUserResetBlockMinutes = (Integer) findParameter("GRYF_IND_USER_RESET_BLOCK_MINUTES");
        if (dbIndUserResetBlockMinutes != null) {
            indUserResetBlockMinutes = dbIndUserResetBlockMinutes;
        }
        Integer dbResetLinkActiveMinutes = (Integer) findParameter("GRYF_LINK_ACTI");
        if (dbResetLinkActiveMinutes != null) {
            resetLinkActiveMinutes = dbResetLinkActiveMinutes;
        }
        String dbPublicCaptchaKey = (String) findParameter("GRYF_PUBLIC_CAPTCHA_KEY");
        if (dbPublicCaptchaKey != null) {
            publicCaptchaKey = dbPublicCaptchaKey;
        }
        String dbSecretCaptchaKey = (String) findParameter("GRYF_SECRET_CAPTCHA_KEY");
        if (dbSecretCaptchaKey != null) {
            secretCaptchaKey = dbSecretCaptchaKey;
        }
        String dbIndUserContext = (String) findParameter("GRYF_IND_USER_CONTEXT");
        if (dbIndUserContext != null) {
            indUserContext = dbIndUserContext;
        }
        String dbTiUserContext = (String) findParameter("GRYF_TI_USER_CONTEXT");
        if (dbTiUserContext != null) {
            tiUserContext = dbTiUserContext;
        }
        String dbPathDataImport = (String) findParameter("GRYF_PATH_DATA_IMPORT");
        if (dbPathDataImport != null) {
            pathDataImport = dbPathDataImport;
        }
        String dbPathAccountingDocument = (String) findParameter("GRYF_PATH_ACCOUNTING_DOCUMENT");
        if (dbPathAccountingDocument != null) {
            pathAccountingDocument = dbPathAccountingDocument;
        }
        String dbPathEreimbursements = (String) findParameter("GRYF_PATH_EREIMBURSEMENTS");
        if (dbPathEreimbursements != null) {
            pathEreimbursements = dbPathEreimbursements;
        }
        String dbIndUserUrl = (String) findParameter("GRYF_IND_USER_URL");
        if(dbIndUserUrl != null){
            indUserUrl = dbIndUserUrl;
        }
        String dbTiUserUrl = (String) findParameter("GRYF_TI_USER_URL");
        if(dbTiUserUrl != null){
            tiUserUrl = dbTiUserUrl;
        }
        Integer dbBusinessDaysNumberForReimbursement = (Integer) findParameter("GRYF_BUSINESS_DAYS_NUM_FOR_REIMB");
        if (dbBusinessDaysNumberForReimbursement != null) {
            businessDaysNumberForReimbursement = dbBusinessDaysNumberForReimbursement;
        }
        Integer dbBusinessDaysNumberForCorrection = (Integer) findParameter("GRYF_BUSINESS_DAYS_NUM_FOR_CORR");
        if (dbBusinessDaysNumberForCorrection != null) {
            businessDaysNumberForCorrection = dbBusinessDaysNumberForCorrection;
        }
        String dbErmbsEmailAttachmentDirectory = (String) findParameter("GRYF_ERMBS_EMAIL_ATTACHMENT_DIRECTORY");
        if (dbErmbsEmailAttachmentDirectory != null) {
            ermbsEmailAttachmentDirectory = dbErmbsEmailAttachmentDirectory;
        }

    }

    //PUBLIC METHODS

    @Override
    public String getCdnUrl() {
        return cdnUrl;
    }

    @Override
    public String getResourcesUrl() {
        return resourcesUrl;
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public String getJsUrl() {
        return jsUrl;
    }

    @Override
    public String getTemplatesUrl() {
        return templatesUrl;
    }

    @Override
    public String getPathAttachments() {
        return pathAttachments;
    }

    @Override
    public String getPathGrantApp() {
        return pathGrantApp;
    }

    @Override
    public String getPathOrders() {
        return pathOrders;
    }

    @Override
    public String getPathReportTemplates() {
        return pathReportTemplates;
    }

    @Override
    public String getPathReimbursements() {
        return pathReimbursements;
    }

    @Override
    public String getPathDataImport() {
        return pathDataImport;
    }

    @Override
    public String getPathAccountingDocument(){
        return pathAccountingDocument;
    }

    @Override
    public String getPathEreimbursements() {
        return pathEreimbursements;
    }

    @Override
    public int getAttachmentMaxSize() {
        return attachmentMaxSize;
    }

    @Override
    public int getAttachmentMaxSizeMB() {
        return attachmentMaxSize / 1024 / 1024;
    }

    @Override
    public String getGryfEnterpriseCodePrefix() {
        return gryfEnterpriseCodePrefix;
    }

    @Override
    public String getGryfIndividualCodePrefix() {
        return gryfIndividualCodePrefix;
    }

    @Override
    public int getGryfEnterpriseCodeZeroCount() {
        return gryfEnterpriseCodeZeroCount;
    }

    @Override
    public int getGryfIndividualCodeZeroCount() {
        return gryfIndividualCodeZeroCount;
    }

    @Override
    public String getGryfTrainingInstitutionCodePrefix() {
        return gryfTrainingInstitutionCodePrefix;
    }

    @Override
    public int getGryfTrainingInstitutionCodeZeroCount() {
        return gryfTrainingInstitutionCodeZeroCount;
    }

    @Override
    public String getGryfPbeDefaultSmtpHost() {
        return gryfPbeDefaultSmtpHost;
    }

    @Override
    public String getGryfPbeDefaultSmtpPort() {
        return gryfPbeDefaultSmtpPort;
    }

    @Override
    public String getGryfPbeAdmEmailFrom() {
        return gryfPbeAdmEmailFrom;
    }

    @Override
    public String getGryfPbeAdmEmailReplyTo() {
        return gryfPbeAdmEmailReplyTo;
    }

    @Override
    public String getGryfPbeDefPubEmailFrom() {
        return gryfPbeDefPubEmailFrom;
    }

    @Override
    public String getGryfPbeDefPubEmailReplyTo() {
        return gryfPbeDefPubEmailReplyTo;
    }

    @Override
    public String getSodexoName() {
        return sodexoName;
    }

    @Override
    public String getSodexoAddress1() {
        return sodexoAddress1;
    }

    @Override
    public String getSodexoAddress2() {
        return sodexoAddress2;
    }

    @Override
    public String getSodexoVatRegNum() {
        return sodexoVatRegNum;
    }

    public String getSodexoBankName() {
        return sodexoBankName;
    }

    @Override
    public String getDocumentGeneratePlace() {
        return documentGeneratePlace;
    }

    @Override
    public String getPathReportImages() {
        return pathReportImages;
    }

    @Override
    public String getPrintNumberCountryCodePoland() {
        return printNumberCountryCodePoland;
    }

    @Override
    public Integer getVerificationCodeLength() {
        return verificationCodeLength;
    }

    @Override
    public Integer getMaxLoginFailureAttempts() {
        return maxLoginFailureAttempts;
    }

    @Override
    public Integer getUserLoginBlockMinutes() {
        return userLoginBlockMinutes;
    }

    @Override
    public String getVerEmailContactType() {
        return verEmailContactType;
    }

    @Override
    public Integer getMaxIndResetFailureAttempts() {
        return maxIndResetFailureAttempts;
    }

    @Override
    public Integer getIndUserResetBlockMinutes() {
        return indUserResetBlockMinutes;
    }

    @Override
    public Integer getResetLinkActiveMinutes() {
        return resetLinkActiveMinutes;
    }

    @Override
    public String getPublicCaptchaKey() {
        return publicCaptchaKey;
    }

    @Override
    public String getSecretCaptchaKey() {
        return secretCaptchaKey;
    }

    @Override
    public String getTiUserContext() {
        return tiUserContext;
    }

    @Override
    public String getIndUserContext() {
        return indUserContext;
    }

    @Override
    public String getIndUserUrl(){
        return indUserUrl;
    }

    @Override
    public String getTiUserUrl(){
        return tiUserUrl;
    }

    @Override
    public Integer getBusinessDaysNumberForReimbursement() {
        return businessDaysNumberForReimbursement;
    }

    @Override
    public Integer getBusinessDaysNumberForCorrection() {
        return businessDaysNumberForCorrection;
    }

    @Override
    public String getErmbsEmailAttachmentDirectory() {
        return ermbsEmailAttachmentDirectory;
    }

    //PRIVATE METHODS

    private Object findParameter(String name) {
        Query query = em.createNativeQuery("SELECT p.value FROM EAGLE.ADM_PARAMETERS p WHERE P.NAME = ? AND ROWNUM = 1");
        query.setParameter(1, name);
        List<Object> result = query.getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

}
