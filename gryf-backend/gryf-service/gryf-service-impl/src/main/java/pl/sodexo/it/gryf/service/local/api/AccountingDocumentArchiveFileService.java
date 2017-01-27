package pl.sodexo.it.gryf.service.local.api;

/**
 * Created by Isolution on 2017-01-27.
 */
public interface AccountingDocumentArchiveFileService {

    String createAccountingDocument(String invoiceNumber, String sourcePath);

}
