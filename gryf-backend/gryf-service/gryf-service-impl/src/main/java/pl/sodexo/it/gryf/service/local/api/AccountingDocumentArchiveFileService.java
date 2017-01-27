package pl.sodexo.it.gryf.service.local.api;

/**
 * Created by Isolution on 2017-01-27.
 */
public interface AccountingDocumentArchiveFileService {

    Long createAccountingDocument(Long invoiceId, String invoiceNumber, String sourcePath, String reportParameters);

}
