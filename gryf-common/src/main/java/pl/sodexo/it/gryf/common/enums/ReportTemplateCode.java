package pl.sodexo.it.gryf.common.enums;

/**
 * Created by tomasz.bilski.ext on 2015-09-14.
 */
public enum ReportTemplateCode {

    INVOICE("Invoice.jasper"),
    GRANT_ACKNOWLEDGMENT_REPORT("GrantAcknowledgmentReport.jasper"),
    ENTERPRISE_AGREEMENT("EnterpriseAgreementReport.jasper"),
    DEBIT_NOTE("DebitNote.jasper", "Nota obciążeniowo-ksiegowa"),
    CREDIT_NOTE("CreditNote.jasper", "Nota uznaniowa"),
    BANK_TRANSFER_CONFIRMATION("BankTransferConfirmation.jasper", "Potwierdzenie wpłaty należnosci"),
    GRANT_AID_CONFIRMATION("GrantAidConfirmation.jasper", "Potwierdzenie realizacji dofinansowania");

    //FIELDS

    private String fileName;

    private String typeName;

    //CONSTRUCTORS

    ReportTemplateCode(String fileName){
        this.fileName = fileName;
    }

    ReportTemplateCode(String fileName, String typeName){
        this.fileName = fileName;
        this.typeName = typeName;
    }

    //GETTERS

    public String getFileName() {
        return fileName;
    }

    public String getTypeName() {
        return typeName;
    }
}
