package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ErmbsAttachmentEntityMapper extends VersionableEntityMapper<ErmbsAttachment,ErmbsAttachmentDto> {

    @Override
    protected ErmbsAttachmentDto initDestination() {
        return new ErmbsAttachmentDto();
    }

    @Override
    public void map(ErmbsAttachment entity, ErmbsAttachmentDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setErmbsId(entity.getEreimbursement() != null ? entity.getEreimbursement().getId() : null);
        dto.setCorrId(entity.getCorrection() != null ? entity.getCorrection().getId() : null);
        dto.setCode(entity.getAttachmentType().getId());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setAdditionalDescription(entity.getAdditionalDescription());
        dto.setOriginalFileName(entity.getOrginalFileName());
        dto.setFileLocation(entity.getFileLocation());
    }
}
