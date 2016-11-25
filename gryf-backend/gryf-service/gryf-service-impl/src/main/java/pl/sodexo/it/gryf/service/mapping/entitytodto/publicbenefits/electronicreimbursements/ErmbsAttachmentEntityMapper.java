package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentsDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ErmbsAttachmentEntityMapper extends GryfEntityMapper<ErmbsAttachment,ErmbsAttachmentsDto> {

    @Override
    protected ErmbsAttachmentsDto initDestination() {
        return new ErmbsAttachmentsDto();
    }

    @Override
    public void map(ErmbsAttachment entity, ErmbsAttachmentsDto dto) {
        super.map(entity, dto);
        dto.setErmbsId(entity.getEreimbursement() != null ? entity.getEreimbursement().getId() : null);
        dto.setCorrId(entity.getCorrection() != null ? entity.getCorrection().getId() : null);
        dto.setCode(entity.getAttachmentType().getId());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setAdditionalDescription(entity.getAdditionalDescription());
        dto.setOrginalFileName(entity.getOrginalFileName());
        dto.setFileLocation(entity.getFileLocation());
    }
}
