/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.service.impl.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.service.api.other.ApplicationParametersService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class ApplicationParametersServiceImpl implements ApplicationParametersService {
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    
    private String cdnUrl = "//cdn.sodexo.pl/"; //default
    private String resourcesUrl = "/resources/"; //default
    private int sessionTimeout = 30;
    private String jsUrl = "/js/";
    private String templatesUrl = "/templates/";
    private String pathAttachments = "\\\\ceplsvc-wawdb20\\gryf\\";
    private String pathGrantApp = "grant_applications\\dev\\";
    private String pathOrders  = "orders\\dev\\";
    private String pathReportTemplates  = "report_templates\\";
    private String pathReimbursements = "reimbursements\\dev\\";
    private int attachmentMaxSize = 3145728;
    private String gryfEnterpriseCodePrefix = "6";
    private int gryfEnterpriseCodeZeroCount = 7;
    private String gryfIndividualCodePrefix = "7";
    private int gryfIndividualCodeZeroCount = 7;
    private String gryfTrainingInstitutionCodePrefix = "8";
    private int gryfTrainingInstitutionCodeZeroCount = 7;
    private EmailTemplate stdEmailTemplate = null;
    private String stdEmailBodyTemplate = "{$emailBody}";
    private String stdEmailSubjectTemplate = "{$emailSubject}";
    private String gryfPbeDefaultSmtpHost = "plsrv-5e.sodexhopass.polska";
    private String gryfPbeDefaultSmtpPort = "25";
    private String gryfPbeAdmEmailFrom = "gryf@sodexo.com.pl";
    private String gryfPbeAdmEmailReplyTo = "es@sodexomotivation.pl";
    private String gryfPbeDefPubEmailFrom = "tbok.pfk@sodexo.com.pl";
    private String gryfPbeDefPubEmailReplyTo = "tbok.pfk@sodexo.com";
    private String sodexoName = "Sodexo Benefits and Rewards Services Polska Sp. z o.o.";
    private String sodexoAddress1 = "ul. Kłobucka 25";
    private String sodexoAddress2 = "02-699 Warszawa";
    private String sodexoVatRegNum = "5222357343";
    private String pathReportImages = "report_images\\";

    //LIFECYCLE METHODS

    @PostConstruct
    private void queryParameters() {
        String dbCdnUrl = (String)findParameter("GRYF_CDN_URL");
        if (dbCdnUrl != null) {
            cdnUrl = dbCdnUrl;
        }
        String dbResourcesUrl = (String)findParameter("GRYF_RESOURCES_URL");
        if (dbResourcesUrl != null) {
            resourcesUrl = dbResourcesUrl;
        }
        String dbSessionTimeout = (String)findParameter("GRYF_WEB_SESSION_TIMEOUT");
        if (dbSessionTimeout != null) {
            sessionTimeout = Integer.valueOf(dbSessionTimeout);
        }
        String dbJsUrl = (String)findParameter("GRYF_JS_URL");
        if (dbJsUrl != null) {
            jsUrl = dbJsUrl;
        }
        String dbTemplatesUrl = (String)findParameter("GRYF_TEMPLATES_URL");
        if (dbTemplatesUrl != null) {
            templatesUrl = dbTemplatesUrl;
        }
        String dbPathAttachments = (String)findParameter("GRYF_PATH_ATTACHMENTS");
        if (dbPathAttachments != null) {
            pathAttachments = dbPathAttachments;
        }
        String dbPathGrantApp = (String)findParameter("GRYF_PATH_GRANT_APP");
        if (dbPathGrantApp != null) {
            pathGrantApp = dbPathGrantApp;
        }
        String dbPathOrders = (String)findParameter("GRYF_PATH_ORDERS");
        if (dbPathOrders != null) {
            pathOrders = dbPathOrders;
        }
        String dbPathReportTemplates = (String)findParameter("GRYF_PATH_REPORT_TEMPLATES");
        if (dbPathReportTemplates != null) {
            pathReportTemplates = dbPathReportTemplates;
        }
        String dbPathReimbursements = (String)findParameter("GRYF_PATH_REIMBURSEMENTS");
        if (dbPathReimbursements != null) {
            pathReimbursements = dbPathReimbursements;
        }
        String dbAttachmentMaxSize = (String)findParameter("GRYF_ATTACHMENT_MAX_SIZE");
        if (dbAttachmentMaxSize != null) {
            attachmentMaxSize = Integer.valueOf(dbAttachmentMaxSize);
        }
        String dbGryfEnterpriseCodePrefix = (String)findParameter("GRYF_ENTERPRISE_CODE_PREFIX");
        if (dbGryfEnterpriseCodePrefix != null) {
            gryfEnterpriseCodePrefix = dbGryfEnterpriseCodePrefix;
        }
        String dbGryfIndividualCodePrefix = (String)findParameter("GRYF_INDIVIDUAL_CODE_PREFIX");
        if (dbGryfIndividualCodePrefix != null) {
            gryfIndividualCodePrefix = dbGryfIndividualCodePrefix;
        }
        String dbGryfEnterpriseCodeZeroCount = (String)findParameter("GRYF_ENTERPRISE_CODE_ZERO_COUNT");
        if (dbGryfEnterpriseCodeZeroCount != null) {
            gryfEnterpriseCodeZeroCount = Integer.valueOf(dbGryfEnterpriseCodeZeroCount);
        }
        String dbGryfIndividualCodeZeroCount = (String)findParameter("GRYF_INDIVIDUAL_CODE_ZERO_COUNT");
        if (dbGryfIndividualCodeZeroCount != null) {
            gryfIndividualCodeZeroCount = Integer.valueOf(dbGryfIndividualCodeZeroCount);
        }
        String dbGryfTrainingInstitutionCodePrefix = (String)findParameter("GRYF_TRAINING_INSTITUTION_CODE_PREFIX");
        if (dbGryfTrainingInstitutionCodePrefix != null) {
            gryfTrainingInstitutionCodePrefix = dbGryfTrainingInstitutionCodePrefix;
        }
        String dbGryfTrainingInstitutionCodeZeroCount = (String)findParameter("GRYF_TRAINING_INSTITUTION_CODE_ZERO_COUNT");
        if (dbGryfTrainingInstitutionCodeZeroCount != null) {
            gryfTrainingInstitutionCodeZeroCount = Integer.valueOf(dbGryfTrainingInstitutionCodeZeroCount);
        }
        EmailTemplate dbStdEmailTemplate = emailTemplateRepository.get(EmailTemplate.STD_EMAIL);
        if(dbStdEmailTemplate != null){
            stdEmailTemplate = dbStdEmailTemplate;
            stdEmailSubjectTemplate = dbStdEmailTemplate.getEmailSubjectTemplate();
            stdEmailBodyTemplate = dbStdEmailTemplate.getEmailBodyTemplate();
        }
        String dbGryfPbeDefaultSmtpHost = (String)findParameter("GRYF_PBE_DEFAULT_SMTP_HOST");
        if (dbGryfPbeDefaultSmtpHost != null) {
            gryfPbeDefaultSmtpHost = dbGryfPbeDefaultSmtpHost;
        }
        String dbGryfPbeDefaultSmtpPort = (String)findParameter("GRYF_PBE_DEFAULT_SMTP_PORT");
        if (dbGryfPbeDefaultSmtpPort != null) {
            gryfPbeDefaultSmtpPort = dbGryfPbeDefaultSmtpPort;
        }
        String dbGryfPbeAdmEmailFrom = (String)findParameter("GRYF_PBE_ADM_EMAIL_FROM");
        if (dbGryfPbeAdmEmailFrom != null) {
            gryfPbeAdmEmailFrom = dbGryfPbeAdmEmailFrom;
        }
        String dbGryfPbeAdmEmailReplyTo = (String)findParameter("GRYF_PBE_ADM_EMAIL_REPLY_TO");
        if (dbGryfPbeAdmEmailReplyTo != null) {
            gryfPbeAdmEmailReplyTo = dbGryfPbeAdmEmailReplyTo;
        }
        String dbGryfPbeDefPubEmailFrom = (String)findParameter("GRYF_PBE_DEF_PUB_EMAIL_FROM");
        if (dbGryfPbeDefPubEmailFrom != null) {
            gryfPbeDefPubEmailFrom = dbGryfPbeDefPubEmailFrom;
        }
        String dbGryfPbeDefPubEmailReplyTo = (String)findParameter("GRYF_PBE_DEF_PUB_EMAIL_REPLYTO");
        if (dbGryfPbeDefPubEmailReplyTo != null) {
            gryfPbeDefPubEmailReplyTo = dbGryfPbeDefPubEmailReplyTo;
        }
        String dbSodexoName = null;//TODO: pobrac z bazy
        if (dbSodexoName != null) {
            sodexoName = dbSodexoName;
        }
        String dbSodexoAddress1 = null;//TODO: pobrac z bazy
        if (dbSodexoAddress1 != null) {
            sodexoAddress1 = dbSodexoAddress1;
        }
        String dbSodexoAddress2 = null;//TODO: pobrac z bazy
        if (dbSodexoAddress2 != null) {
            sodexoAddress2 = dbSodexoAddress2;
        }
        String dbSodexoVatRegNum = null;//TODO: pobrac z bazy
        if (dbSodexoVatRegNum != null) {
            sodexoVatRegNum = dbSodexoVatRegNum;
        }
        String dbPathReportImages = (String)findParameter("GRYF_PATH_REPORT_IMAGES");
        if (dbPathReportImages != null) {
            pathReportImages = dbPathReportImages;
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
    public EmailTemplate getStdEmailTemplate() {
        return stdEmailTemplate;
    }

    @Override
    public String getStdEmailBodyTemplate() {
        return stdEmailBodyTemplate;
    }

    @Override
    public String getStdEmailSubjectTemplate() {
        return stdEmailSubjectTemplate;
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

    @Override
    public String getPathReportImages() {
        return pathReportImages;
    }

    
    
    //PRIVATE METHODS

    private Object findParameter(String name){
        Query query = em.createNativeQuery("SELECT p.value FROM EAGLE.ADM_PARAMETERS p WHERE P.NAME = ? AND ROWNUM = 1");
        query.setParameter(1, name);
        List<Object> result = query.getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

}
