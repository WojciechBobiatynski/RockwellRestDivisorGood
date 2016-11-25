package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentsDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ErmbsAttachmentsDto na encję ErmbsAttachment
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class ErmbsAttachmentDtoMapper extends VersionableDtoMapper<ErmbsAttachmentsDto, ErmbsAttachment> {

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private AttachmentTypeRepository attachmentTypeRepository;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Override
    protected ErmbsAttachment initDestination() {
        return new ErmbsAttachment();
    }

    @Override
    protected void map(ErmbsAttachmentsDto dto, ErmbsAttachment entity) {
        super.map(dto, entity);
        entity.setEreimbursement(dto.getErmbsId() != null ? ereimbursementRepository.get(dto.getErmbsId()) : null);
        entity.setCorrection(dto.getCorrId() != null ? correctionRepository.get(dto.getCorrId()) : null);
        entity.setAttachmentType(attachmentTypeRepository.get(dto.getCode()));
        entity.setDocumentNumber(dto.getDocumentNumber());
        entity.setAdditionalDescription(dto.getAdditionalDescription());
        entity.setOrginalFileName(dto.getOrginalFileName());
        entity.setFileLocation(dto.getFileLocation());
    }

}
