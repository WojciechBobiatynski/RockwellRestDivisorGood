package pl.sodexo.it.gryf.service.local.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.service.local.api.AccountingDocumentArchiveFileService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.util.Date;

/**
 * Created by Isolution on 2017-01-27.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class AccountingDocumentArchiveFileServiceImpl implements AccountingDocumentArchiveFileService {

    @Autowired
    private FileService fileService;

    @Override
    public String createAccountingDocument(String invoiceNumber, String sourcePath) {

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

}
