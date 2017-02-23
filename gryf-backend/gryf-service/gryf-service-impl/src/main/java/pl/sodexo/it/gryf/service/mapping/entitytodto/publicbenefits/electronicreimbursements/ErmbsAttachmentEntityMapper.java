package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ErmbsAttachmentEntityMapper extends GenericMapper<ErmbsAttachment,ErmbsAttachmentDto> {

    @Override
    protected ErmbsAttachmentDto initDestination() {
        return new ErmbsAttachmentDto();
    }

    @Override
    public void map(ErmbsAttachment entity, ErmbsAttachmentDto dto) {
        dto.setId(entity.getId());
        dto.setErmbsId(entity.getEreimbursement() != null ? entity.getEreimbursement().getId() : null);
        dto.setCode(entity.getAttachmentType().getId());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setDocumentDate(entity.getDocumentDate());
        dto.setAdditionalDescription(entity.getAdditionalDescription());
        dto.setFileId(entity.getAttachmentFile() != null ? entity.getAttachmentFile().getId() : null);
        dto.setStatus(entity.getStatus());
        dto.setVersion(entity.getVersion());
    }
}
