package pl.sodexo.it.gryf.service.local.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.FileType;
import pl.sodexo.it.gryf.service.api.ApplicationParametersService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-09.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    //FIELDS

    @Autowired
    private ApplicationParametersService parameterService;

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
            throw new RuntimeException(String.format("Nie udało się zapisać pliku [%s]", filePath), e);
        }
        return filePath;
    }

    @Override
    public void deleteFile(String fileName) {
        if(!StringUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
    }

    @Override
    public void deleteSavedFiles(List<FileDTO> files){
        for (FileDTO file : files) {
            if(!StringUtils.isEmpty(file.getFileLocation())){
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
            default:
                throw new RuntimeException(String.format("Nieznany typ pliku: [%s]", fileType));
        }
    }

    //PRIVATE METHODS

    private String findFileLocation(FileType fileType, String fileName, FileDTO fileDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(findPath(fileType)).append(fileName);
        String fileExtension = StringUtils.findFileExtension(fileDTO.getOriginalFilename());
        if (!StringUtils.isEmpty(fileExtension)) {
            sb.append(".").append(fileExtension);
        }
        return sb.toString();
    }

}