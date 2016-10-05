package pl.sodexo.it.gryf.common.enums;

/**
 * Created by tomasz.bilski.ext on 2015-09-14.
 */
public enum ReportTemplateCode {

    INVOICE("Invoice.jasper"),
    GRANT_ACKNOWLEDGMENT_REPORT("GrantAcknowledgmentReport.jasper"),
    ENTERPRISE_AGREEMENT("EnterpriseAgreementReport.jasper");

    //FIELDS

    private String fileName;

    //CONSTRUCTORS

    private ReportTemplateCode(String fileName){
        this.fileName = fileName;
    }

    //GETTERS

    public String getFileName() {
        return fileName;
    }

}
