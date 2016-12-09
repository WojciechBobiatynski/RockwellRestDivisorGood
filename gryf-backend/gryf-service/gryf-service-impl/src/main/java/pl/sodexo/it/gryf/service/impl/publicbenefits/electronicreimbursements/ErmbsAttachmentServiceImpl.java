package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
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
    public void manageErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentStatus status) {
        fileAttachmentService.manageAttachmentFiles(elctRmbsHeadDto);
        manageErmbsAttachmentsEntity(elctRmbsHeadDto, status);
    }

    @Override
    public void manageErmbsAttachmentsForCorrection(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentStatus status) {
        fileAttachmentService.manageAttachmentFilesForCorrections(elctRmbsHeadDto);
        manageErmbsAttachmentsEntity(elctRmbsHeadDto, status);
    }

    private void manageErmbsAttachmentsEntity(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentStatus status) {
        for (ErmbsAttachmentDto ermbsAttachment : elctRmbsHeadDto.getAttachments()) {
            manageEntity(elctRmbsHeadDto, ermbsAttachment, status);
        }
    }

    private Long manageEntity(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachment, ErmbsAttachmentStatus status) {
        ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
        //oznaczone do usunięcia mogą być tylko pliki już wczesniej zapisane w bazie, a usuwamy tylko te tymczasowe
        if (ermbsAttachment.isMarkToDelete()) {
            if (ErmbsAttachmentStatus.SENDED.equals(ermbsAttachment.getStatus())) {
                entity.setStatus(ErmbsAttachmentStatus.DELETED);
            } else {
                ereimbursementAttachmentRepository.delete(entity);
                return ermbsAttachment.getId();
            }
        }
        entity.setEreimbursement(ereimbursementDtoMapper.convert(elctRmbsHeadDto));
        // możemy zmieniać status załącznika tylko gdy jeszcze go nie ma lub gdy jest to załącznik tymczasowy (zapisz bez wyślij)
        if (entity.getStatus() == null || entity.getStatus().equals(ErmbsAttachmentStatus.TEMP)) {
            entity.setStatus(status);
        }
        saveOrUpdateEntity(entity);
        return entity.getId();
    }

    private void saveOrUpdateEntity(ErmbsAttachment entity) {
        if (entity.getId() != null) {
            ereimbursementAttachmentRepository.update(entity, entity.getId());
        } else {
            ereimbursementAttachmentRepository.save(entity);
        }
    }

}
