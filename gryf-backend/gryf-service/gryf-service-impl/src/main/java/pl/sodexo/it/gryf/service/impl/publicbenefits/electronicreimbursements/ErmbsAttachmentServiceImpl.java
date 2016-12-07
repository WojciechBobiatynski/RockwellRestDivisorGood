package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.AttachmentParentType;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.api.attachments.FileAttachmentService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsAttachmentDtoMapper;

/**
 * Serwis dla operacji na załącznikach rozliczenia bonów elektornicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class ErmbsAttachmentServiceImpl implements ErmbsAttachmentService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ErmbsAttachmentDtoMapper ermbsAttachmentDtoMapper;

    @Autowired
    private EreimbursementAttachmentRepository ereimbursementAttachmentRepository;

    @Autowired
    private EreimbursementDtoMapper ereimbursementDtoMapper;

    @Autowired
    private FileAttachmentService fileAttachmentService;

    @Override
    public FileDTO getErmbsAttFileById(Long id) {
        ErmbsAttachment attachment = ereimbursementAttachmentRepository.get(id);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getAttachmentFile().getOrginalFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getAttachmentFile().getFileLocation()));
        return dto;
    }

    @Override
    public void saveErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            if (deleteIfMarked(ermbsAttachment, false))
                continue;
            Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            saveFile(ermbsAttachment, entity, ereimbursement);
            saveAttachmentEntity(ereimbursement, entity);
        }
    }

    @Override
    public void manageErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto) {
        fileAttachmentService.manageAttachmentFiles(elctRmbsHeadDto);
        manageErmbsAttachmentsEntity(elctRmbsHeadDto);
    }

    private void manageErmbsAttachmentsEntity(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            if (ermbsAttachment.isMarkToDelete()) {
                //oznaczone do usunięcia mogą być tylko pliki już wczesniej zapisane w bazie
                ereimbursementAttachmentRepository.delete(entity);
                continue;
            }
            entity.setEreimbursement(ereimbursementDtoMapper.convert(elctRmbsHeadDto));
            saveOrUpdateEntity(entity);
        }
    }

    private void saveOrUpdateEntity(ErmbsAttachment entity) {
        if (entity.getId() != null) {
            ereimbursementAttachmentRepository.update(entity, entity.getId());
        } else {
            ereimbursementAttachmentRepository.save(entity);
        }
    }

    @Override
    public void saveErmbsAttachmentsForCorr(ElctRmbsHeadDto elctRmbsHeadDto) {

    }

    private boolean deleteIfMarked(ErmbsAttachmentDto ermbsAttachment, boolean leaveFile) {
        if (ermbsAttachment.isMarkToDelete()) {
            if (leaveFile) {
                ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
                ereimbursementAttachmentRepository.update(entity, entity.getId());
                return true;
            }
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            ereimbursementAttachmentRepository.delete(entity);
            return true;
        }
        return false;
    }

    private void saveFile(ErmbsAttachmentDto ermbsAttachment, ErmbsAttachment entity, Ereimbursement ereimbursement) {
        if (ermbsAttachment.isChanged()) {
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
            String fileName = String
                    .format("%s_%s_%s_%s_%s", trainingInstitutionId, ereimbursement.getId(), AttachmentParentType.EREIMB, entity.getId(), ermbsAttachment.getFile().getOriginalFilename());
            String newFileName = GryfStringUtils.convertFileName(fileName);
            String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFile(), entity);
        }
    }

    private ErmbsAttachment saveAttachmentEntity(Ereimbursement ereimbursement, ErmbsAttachment entity) {
        entity.setEreimbursement(ereimbursement);
        entity = entity.getId() != null ? ereimbursementAttachmentRepository.update(entity, entity.getId()) : ereimbursementAttachmentRepository.save(entity);
        return entity;
    }

    private String getNewFileNameForCorr(ErmbsAttachment entity) {
        return fileService.findPath(FileType.E_REIMBURSEMENTS);
    }

    private boolean isTheSameCorrection(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachment entity) {
        return true;
    }

    private void prepareAttEntityToUpdateForCorr(ErmbsAttachmentDto ermbsAttachment, ErmbsAttachment entity) {
        Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
        String fileName = String
                .format("%s_%s_%s_%s_%s", trainingInstitutionId, entity.getEreimbursement().getId(), AttachmentParentType.EREIMB, entity.getId(), ermbsAttachment.getFile().getOriginalFilename());
        String newFileName = GryfStringUtils.convertFileName(fileName);
        String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFile(), entity);
    }

}
