package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentFileRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.CorrectionAttachment;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ErmbsAttachmentDto na encję ErmbsAttachment
 *
 * Created by akmiecinski on 2016-12-08.
 */
@Component
public class CorrectionAttachmentDtoMapper extends VersionableDtoMapper<CorrectionAttachmentDto, CorrectionAttachment> {

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    private EreimbursementAttachmentRepository ereimbursementAttachmentRepository;

    @Override
    protected CorrectionAttachment initDestination() {
        return new CorrectionAttachment();
    }

    @Override
    protected void map(CorrectionAttachmentDto dto, CorrectionAttachment entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setAttachmentFile(dto.getFileId() != null ? attachmentFileRepository.get(dto.getFileId()) : null);
        entity.setCorrection(dto.getCorrId() != null ? correctionRepository.get(dto.getCorrId()) : null);
        entity.setErmbsAttachment(dto.getErmbsAttId() != null ? ereimbursementAttachmentRepository.get(dto.getErmbsAttId()) : null);
        entity.setOldDocumentNumber(dto.getOldDocumentNumber());
        entity.setOldDocumentDate(dto.getOldDocumentDate());
        entity.setOldAdditionalDesc(dto.getOldAdditionalDesc());
    }

}
