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
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsAttachmentDtoMapper;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.ATT_CORR_SUFFIX;
import static pl.sodexo.it.gryf.common.utils.GryfConstants.FILE_EXTENSION_DELIMITER;

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
    private CorrectionRepository correctionRepository;

    @Autowired
    private CorrectionService correctionService;

    @Override
    public FileDTO getErmbsAttFileById(Long id) {
        ErmbsAttachment attachment = ereimbursementAttachmentRepository.get(id);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getOrginalFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    @Override
    public void saveErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            if (deleteIfMarked(ermbsAttachment))
                continue;
            Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
            ErmbsAttachment entity = saveAttachmentEntity(ereimbursement, ermbsAttachment);
            saveFile(ermbsAttachment, entity, ereimbursement);
        }
    }

    @Override
    public void saveErmbsAttachmentsForCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            if (deleteIfMarked(ermbsAttachment))
                continue;
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            if(entity.getId() == null){
                Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
                entity = saveAttachmentEntity(ereimbursement, ermbsAttachment);
                saveFile(ermbsAttachment, entity, ereimbursement);
            } else {
                if(ermbsAttachment.isChanged()){
                    if(isTheSameCorrection(elctRmbsHeadDto, entity)){
                        fileService.deleteFile(ermbsAttachment.getFileLocation());
                        prepareAttEntityToUpdateForCorr(ermbsAttachment, entity);
                    } else {
                        fileService.changeFileName(entity.getFileLocation(), getNewFileNameForCorr(entity));
                        prepareAttEntityToUpdateForCorr(ermbsAttachment, entity);
                    }
                    entity.setCorrection(correctionRepository.get(elctRmbsHeadDto.getLastCorrectionDto().getId()));
                }
                ereimbursementAttachmentRepository.update(entity, entity.getId());
            }
        }

    }

    private String getNewFileNameForCorr(ErmbsAttachment entity) {
        String rootPath = fileService.findPath(FileType.E_REIMBURSEMENTS);
        String fileExtension = FILE_EXTENSION_DELIMITER + GryfStringUtils.findFileExtension(entity.getOrginalFileName());
        Integer correctionsNumbers = correctionService.findCorrectionsNumberByErmbsId(entity.getEreimbursement().getId()) - 1;
        StringBuilder stringBuilder = new StringBuilder(entity.getFileLocation().replace(rootPath, "").replace(fileExtension, ""));
        stringBuilder.append(ATT_CORR_SUFFIX);
        stringBuilder.append(correctionsNumbers);
        stringBuilder.append(fileExtension);
        return stringBuilder.toString();
    }

    private boolean isTheSameCorrection(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachment entity) {
        return entity.getCorrection() != null && entity.getCorrection().getId().equals(elctRmbsHeadDto.getLastCorrectionDto().getId());
    }

    private void prepareAttEntityToUpdateForCorr(ErmbsAttachmentDto ermbsAttachment, ErmbsAttachment entity) {
        Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
        String fileName = String
                .format("%s_%s_%s_%s_%s", trainingInstitutionId, entity.getEreimbursement().getId(), AttachmentParentType.EREIMB, entity.getId(), ermbsAttachment.getFile().getOriginalFilename());
        String newFileName = GryfStringUtils.convertFileName(fileName);
        String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFile(), entity);
        entity.setOrginalFileName(ermbsAttachment.getFile().getOriginalFilename());
        entity.setFileLocation(filePath);
    }

    private boolean deleteIfMarked(ErmbsAttachmentDto ermbsAttachment) {
        if (ermbsAttachment.isMarkToDelete()) {
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            ereimbursementAttachmentRepository.delete(entity);
            return true;
        }
        return false;
    }

    private ErmbsAttachment saveAttachmentEntity(Ereimbursement ereimbursement, ErmbsAttachmentDto ermbsAttachment) {
        ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
        entity.setEreimbursement(ereimbursement);
        entity = entity.getId() != null ? ereimbursementAttachmentRepository.update(entity, entity.getId()) : ereimbursementAttachmentRepository.save(entity);
        return entity;
    }

    private void saveFile(ErmbsAttachmentDto ermbsAttachment, ErmbsAttachment entity, Ereimbursement ereimbursement) {
        if (ermbsAttachment.isChanged()) {
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
            String fileName = String
                    .format("%s_%s_%s_%s_%s", trainingInstitutionId, ereimbursement.getId(), AttachmentParentType.EREIMB, entity.getId(), ermbsAttachment.getFile().getOriginalFilename());
            String newFileName = GryfStringUtils.convertFileName(fileName);
            String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFile(), entity);
            entity.setOrginalFileName(ermbsAttachment.getFile().getOriginalFilename());
            entity.setFileLocation(filePath);
        }
    }

}
