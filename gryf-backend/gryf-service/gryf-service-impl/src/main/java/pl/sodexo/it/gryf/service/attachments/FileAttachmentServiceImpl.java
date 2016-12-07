package pl.sodexo.it.gryf.service.attachments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.AttachmentParentType;
import pl.sodexo.it.gryf.common.enums.FileSystemStatus;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentFileRepository;
import pl.sodexo.it.gryf.model.attachments.AttachmentFile;
import pl.sodexo.it.gryf.service.api.attachments.FileAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;

/**
 * Implementacja serwisu do operacji na plikach załączników
 *
 * Created by akmiecinski on 07.12.2016.
 */
@Service
@Transactional
public class FileAttachmentServiceImpl implements FileAttachmentService {

    @Autowired
    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    private FileService fileService;

    @Override
    public void manageAttachmentFiles(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            if (ermbsAttachment.isMarkToDelete()) {
                //oznaczone do usunięcia mogą być tylko pliki już wczesniej zapisane w bazie
                markFileToDelete(ermbsAttachment);
                continue;
            }
            if (ermbsAttachment.isChanged()) {
                Long newFileId = saveFileAndGetFileEntityId(ermbsAttachment, elctRmbsHeadDto);
                ermbsAttachment.setFileId(newFileId);
            }
        }
    }

    private void markFileToDelete(ErmbsAttachmentDto ermbsAttachment) {
        AttachmentFile attachmentFile = attachmentFileRepository.get(ermbsAttachment.getFileId());
        attachmentFile.setFiletatus(FileSystemStatus.TO_DELETE);
        attachmentFileRepository.update(attachmentFile, attachmentFile.getId());
    }

    private Long saveFileAndGetFileEntityId(ErmbsAttachmentDto ermbsAttachmentDto, ElctRmbsHeadDto elctRmbsHeadDto) {
        Long newAttFileEntityId = createAndSaveNewAttFile(ermbsAttachmentDto, elctRmbsHeadDto);
        markToDeletePreviousAttFileIfExists(ermbsAttachmentDto);
        return newAttFileEntityId;
    }

    private Long createAndSaveNewAttFile(ErmbsAttachmentDto ermbsAttachmentDto, ElctRmbsHeadDto elctRmbsHeadDto) {
        AttachmentFile attachmentFile = new AttachmentFile();
        attachmentFile.setFiletatus(FileSystemStatus.SAVED);
        attachmentFileRepository.save(attachmentFile);
        Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
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
            file.setFiletatus(FileSystemStatus.TO_DELETE);
            attachmentFileRepository.update(file, file.getId());
        }
    }

    private boolean existsPreviousFile(ErmbsAttachmentDto ermbsAttachmentDto) {
        return ermbsAttachmentDto.getFileId() != null;
    }

}
