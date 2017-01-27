package pl.sodexo.it.gryf.service.local.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.invoices.InvoiceArchiveRepository;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.InvoiceArchive;
import pl.sodexo.it.gryf.service.local.api.AccountingDocumentArchiveFileService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.util.Date;

/**
 * Created by Isolution on 2017-01-27.
 */
@Service
public class AccountingDocumentArchiveFileServiceImpl implements AccountingDocumentArchiveFileService {

    @Autowired
    private FileService fileService;

    @Autowired
    private InvoiceArchiveRepository invoiceArchiveRepository;

    //PUBLIC METHODS

    @Override
    public Long createAccountingDocument(Long invoiceId, String invoiceNumber,
                                            String sourcePath, String reportParameters) {

        //ZAPIS PLIKU DO ARCHIWUM
        String archiveFilePath = saveInvoiceArchiveFile(invoiceNumber, sourcePath);

        //ZAPIS INFORMACJI O ARCHIWUM DO BAZY
        return saveInvoiceArchiveData(invoiceId, archiveFilePath, reportParameters);
    }

    private String saveInvoiceArchiveFile(String invoiceNumber, String sourcePath){
        String mainDirectoryPath = fileService.findPath(FileType.ACCOUNTING_DOCUMENT_ARCHIVE);
        String datePath = fileService.createDateFolderPath(new Date());
        String directoryPath = String.format(mainDirectoryPath, datePath);
        fileService.createDirectories(directoryPath);

        String fileName = GryfStringUtils.convertFileName(invoiceNumber);
        String extension = GryfStringUtils.findFileExtension(sourcePath);
        String fileNameWithExtension = fileName + GryfConstants.FILE_EXTENSION_DELIMITER + extension;

        fileService.copyFile(sourcePath, directoryPath, fileNameWithExtension);
        return directoryPath + fileNameWithExtension;
    }

    private Long saveInvoiceArchiveData(Long invoiceId, String archiveFilePath, String reportParameters){
        Date now = new Date();
        InvoiceArchive ia = new InvoiceArchive();
        ia.setSourcePath(archiveFilePath);
        ia.setGenDate(now);
        ia.setInvoiceId(invoiceId);
        ia.setGenerated(true);
        ia.setChecked(true);
        ia.setCheckDate(now);
        ia.setUrl(GryfStringUtils.substring(reportParameters, 0, InvoiceArchive.URL_MAX_SIZE));
        ia.setInvMode(InvoiceArchive.INV_MODE_ORGINAL);
        ia.setDuplicate(false);
        ia.setDuplicateDate(null);
        ia.setAttributeId(null);
        ia.setDocumentSourceSystem(InvoiceArchive.DOCUMENT_SOURCE_SYSTEM_GRYF);
        ia = invoiceArchiveRepository.save(ia);
        return ia.getId();
    }

}
