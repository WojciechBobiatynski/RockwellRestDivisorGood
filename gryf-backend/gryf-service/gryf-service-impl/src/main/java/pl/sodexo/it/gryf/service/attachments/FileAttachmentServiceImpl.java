package pl.sodexo.it.gryf.service.attachments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.attachments.AttachmentFileDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.enums.AttachmentParentType;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.common.enums.FileStatus;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentFileRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.AttachmentFileSearchDao;
import pl.sodexo.it.gryf.model.attachments.AttachmentFile;
import pl.sodexo.it.gryf.service.api.attachments.FileAttachmentService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.util.List;

/**
 * Implementacja serwisu do operacji na plikach załączników
 *
 * Created by akmiecinski on 07.12.2016.
 */
@Service
@Transactional
public class FileAttachmentServiceImpl implements FileAttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileAttachmentServiceImpl.class);

    @Autowired
    private AttachmentFileSearchDao attachmentFileSearchDao;

    @Autowired
    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CorrectionAttachmentService correctionAttachmentService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnnecessaryFiles(){
        List<AttachmentFileDto> attachmentsToDelete = attachmentFileSearchDao.findAttachmentsToDelete();
        attachmentsToDelete.stream().forEach(attachmentFileDto -> {
            AttachmentFile attachmentFile = attachmentFileRepository.get(attachmentFileDto.getId());
            fileService.deleteFile(attachmentFile.getFileLocation());
            attachmentFileRepository.delete(attachmentFile);
            LOGGER.info("File deleted={}", attachmentFile);
        });
    }

    @Override
    public void manageAttachmentFiles(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            manageFile(elctRmbsHeadDto, ermbsAttachment);
        }
    }

    @Override
    public void manageFile(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachment) {
        if (ermbsAttachment.isMarkToDelete()) {
            //oznaczone do usunięcia mogą być tylko pliki już wczesniej zapisane w bazie
            markFileToDelete(ermbsAttachment);
            return;
        }
        if (ermbsAttachment.isChanged()) {
            Long newFileId = saveNewFileAndGetFileEntityId(ermbsAttachment, elctRmbsHeadDto);
            markToDeletePreviousAttFileIfExists(ermbsAttachment);
            ermbsAttachment.setFileId(newFileId);
        }
    }

    @Override
    public void manageAttachmentFilesForCorrections(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            if (isExsistedAtt(ermbsAttachment) && isFirstChangeOfAttachmentFileInCorrection(elctRmbsHeadDto, ermbsAttachment)
                    && ermbsAttachment.getStatus().equals(ErmbsAttachmentStatus.SENDED)) {
                if (ermbsAttachment.isMarkToDelete()) {
                    markFileToDeleteForCorr(ermbsAttachment);
                    continue;
                }
                if (ermbsAttachment.isChanged()) {
                    Long newFileId = saveNewFileAndGetFileEntityId(ermbsAttachment, elctRmbsHeadDto);
                    markToCorrectPreviousAttFileIfExists(ermbsAttachment);
                    ermbsAttachment.setFileId(newFileId);
                }
            } else {
                manageFile(elctRmbsHeadDto, ermbsAttachment);
            }
        }
    }

    private boolean isExsistedAtt(ErmbsAttachmentDto ermbsAttachment) {
        return ermbsAttachment.getId() != null;
    }

    private void markToCorrectPreviousAttFileIfExists(ErmbsAttachmentDto ermbsAttachmentDto) {
        if (existsPreviousFile(ermbsAttachmentDto)) {
            AttachmentFile file = attachmentFileRepository.get(ermbsAttachmentDto.getFileId());
            file.setFiletatus(FileStatus.CORRECT);
            attachmentFileRepository.update(file, file.getId());
        }
    }

    private boolean isFirstChangeOfAttachmentFileInCorrection(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto) {
        CorrectionAttachmentDto correctionAttachment = correctionAttachmentService.getCorrAttByAttByErmbsAttIdAndCorrId(elctRmbsHeadDto, ermbsAttachmentDto);
        return correctionAttachment == null || correctionAttachment.getFileId() == ermbsAttachmentDto.getFileId();
    }

    private void markFileToDelete(ErmbsAttachmentDto ermbsAttachment) {
        AttachmentFile attachmentFile = attachmentFileRepository.get(ermbsAttachment.getFileId());
        attachmentFile.setFiletatus(FileStatus.TO_DELETE);
        attachmentFileRepository.update(attachmentFile, attachmentFile.getId());
    }

    private void markFileToDeleteForCorr(ErmbsAttachmentDto ermbsAttachment) {
        AttachmentFile attachmentFile = attachmentFileRepository.get(ermbsAttachment.getFileId());
        attachmentFile.setFiletatus(FileStatus.CORRECT);
        attachmentFileRepository.update(attachmentFile, attachmentFile.getId());
    }

    private Long saveNewFileAndGetFileEntityId(ErmbsAttachmentDto ermbsAttachmentDto, ElctRmbsHeadDto elctRmbsHeadDto) {
        Long newAttFileEntityId = createAndSaveNewAttFile(ermbsAttachmentDto, elctRmbsHeadDto);
        return newAttFileEntityId;
    }

    private Long createAndSaveNewAttFile(ErmbsAttachmentDto ermbsAttachmentDto, ElctRmbsHeadDto elctRmbsHeadDto) {
        AttachmentFile attachmentFile = new AttachmentFile();
        attachmentFile.setFiletatus(FileStatus.SAVED);
        attachmentFileRepository.save(attachmentFile);
        Long trainingInstitutionId = elctRmbsHeadDto.getTrainingInstitutionId();
        String fileName = String.format("%s_%s_%s_%s_%s", trainingInstitutionId, elctRmbsHeadDto.getErmbsId(), AttachmentParentType.EREIMB, attachmentFile.getId(), ermbsAttachmentDto.getCode());
        String newFileName = GryfStringUtils.convertFileName(fileName);
        String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachmentDto.getFile(), attachmentFile);
        attachmentFile.setFileLocation(filePath);
        attachmentFile.setOrginalFileName(ermbsAttachmentDto.getFile().getOriginalFilename());
        return attachmentFile.getId();
    }

    private void markToDeletePreviousAttFileIfExists(ErmbsAttachmentDto ermbsAttachmentDto) {
        if (existsPreviousFile(ermbsAttachmentDto)) {
            AttachmentFile file = attachmentFileRepository.get(ermbsAttachmentDto.getFileId());
            file.setFiletatus(FileStatus.TO_DELETE);
            attachmentFileRepository.update(file, file.getId());
        }
    }

    private boolean existsPreviousFile(ErmbsAttachmentDto ermbsAttachmentDto) {
        return ermbsAttachmentDto.getFileId() != null;
    }

}
