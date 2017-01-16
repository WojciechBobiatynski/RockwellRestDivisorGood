package pl.sodexo.it.gryf.service.local.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.exception.GryfFileException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.api.AuditableEntity;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-09.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    //FIELDS

    @Autowired
    private ApplicationParameters parameterService;

    //PUBLIC METHODS

    @Override
    public String writeFile(FileType fileType, String fileName, FileDTO fileDTO, AuditableEntity entity) {
        String filePath = findFileLocation(fileType, fileName, fileDTO);

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            InputStream is = fileDTO.getInputStream();
            GryfUtils.copyStream(is, fos);
            is.close();
            fos.close();
            fileDTO.setFileLocation(filePath);
            if(entity != null) {
                entity.setModifiedTimestamp(new Date());
            }
        } catch (IOException e) {
            throw new GryfFileException(String.format("Nie udało się zapisać pliku [%s]", filePath), e);
        }
        return filePath;
    }

    @Override
    public String writeFile(FileDTO fileDTO, String newFileRoot) {
        String newFileName = GryfStringUtils.convertFileNameWithExtension(fileDTO.getOriginalFilename());
        Path newFilePath = Paths.get(newFileRoot, newFileName);
        try {
            Files.copy(fileDTO.getInputStream(), newFilePath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Nie udało się zapisać pliku [%s]", newFilePath), e);
        }
        return newFilePath.toString();
    }

    @Override
    public void deleteFile(String fileName) {
        if(!GryfStringUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
    }

    @Override
    public void deleteSavedFiles(List<FileDTO> files){
        for (FileDTO file : files) {
            if(!GryfStringUtils.isEmpty(file.getFileLocation())){
                try {
                    deleteFile(file.getFileLocation());
                }catch(RuntimeException e){
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Nie znależiono pliku o nazwie '%s'", fileName));
        }
    }

    @Override
    public OutputStream getOutputStream(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Nie znależiono pliku o nazwie '%s'", fileName));
        }
    }

    @Override
    public String findPath(FileType fileType) {
        switch (fileType) {
            case GRANT_APPLICATIONS:
                return parameterService.getPathAttachments() + parameterService.getPathGrantApp();
            case ORDERS:
                return parameterService.getPathAttachments() + parameterService.getPathOrders();
            case REPORT_TEMPLATES:
                return parameterService.getPathAttachments() + parameterService.getPathReportTemplates();
            case REIMBURSEMENTS:
                return parameterService.getPathAttachments() + parameterService.getPathReimbursements();
            case E_REIMBURSEMENTS:
                return parameterService.getPathAttachments() + parameterService.getPathEreimbursements();
            case DATA_IMPORT:
                return parameterService.getPathAttachments() + parameterService.getPathDataImport();
            default:
                throw new RuntimeException(String.format("Nieznany typ pliku: [%s]", fileType));
        }
    }

    @Override
    public String copyFile(String sourceFilePath, String newFileRoot) {
        Path oldSourcePath = Paths.get(sourceFilePath);
        Path oldFileName = oldSourcePath.getFileName();
        String newFileName = GryfStringUtils.convertFileNameWithExtension(oldFileName.toString());
        Path newFilePath = Paths.get(newFileRoot, newFileName);
        try (OutputStream os = new FileOutputStream(newFilePath.toFile())) {
            Files.copy(oldSourcePath, os);
        } catch (IOException ex) {
            throw new GryfFileException(String.format("Nie udało się zapisać pliku"), ex);
        }
        return newFilePath.toString();
    }

    //PRIVATE METHODS

    private String findFileLocation(FileType fileType, String fileName, FileDTO fileDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(findPath(fileType)).append(fileName);
        String fileExtension = GryfStringUtils.findFileExtension(fileDTO.getOriginalFilename());
        if (!GryfStringUtils.isEmpty(fileExtension)) {
            sb.append(".").append(fileExtension);
        }
        return sb.toString();
    }

}