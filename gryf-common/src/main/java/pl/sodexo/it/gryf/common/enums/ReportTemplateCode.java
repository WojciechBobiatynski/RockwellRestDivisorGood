package pl.sodexo.it.gryf.common.enums;

/**
 * Created by tomasz.bilski.ext on 2015-09-14.
 */
public enum ReportTemplateCode {

    INVOICE("Invoice.jasper"),
    GRANT_ACKNOWLEDGMENT_REPORT("GrantAcknowledgmentReport.jasper"),
    ENTERPRISE_AGREEMENT("EnterpriseAgreementReport.jasper"),
    DEBIT_NOTE("DebitNote.jasper"),//nota obciążeniowo-ksiegowa
    CREDIT_NOTE("CreditNote.jasper"),//nota uznaniowa
    BANK_TRANSFER_CONFIRMATION("BankTransferConfirmation.jasper"),//potwierdzenie wpłaty należnosci
    GRANT_AID_CONFIRMATION("GrantAidConfirmation.jasper");//Potwierdzenie realizacji dofinansowania

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
