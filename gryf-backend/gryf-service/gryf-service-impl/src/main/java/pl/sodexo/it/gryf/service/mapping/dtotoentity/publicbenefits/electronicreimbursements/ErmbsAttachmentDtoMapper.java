package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ErmbsAttachmentDto na encję ErmbsAttachment
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class ErmbsAttachmentDtoMapper extends VersionableDtoMapper<ErmbsAttachmentDto, ErmbsAttachment> {

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
    protected void map(ErmbsAttachmentDto dto, ErmbsAttachment entity) {
        super.map(dto, entity);
        entity.setEreimbursement(dto.getErmbsId() != null ? ereimbursementRepository.get(dto.getErmbsId()) : null);
        entity.setCorrection(dto.getCorrId() != null ? correctionRepository.get(dto.getCorrId()) : null);
        entity.setAttachmentType(attachmentTypeRepository.get(dto.getCode()));
        entity.setDocumentNumber(dto.getDocumentNumber());
        entity.setAdditionalDescription(dto.getAdditionalDescription());
        entity.setOrginalFileName(dto.getOriginalFileName());
        entity.setFileLocation(dto.getFileLocation());
    }

}
