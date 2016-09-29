package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.YesNo;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachmentRequired;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ReimbursementAttachmentRequiredEntityMapper extends GryfEntityMapper<ReimbursementAttachmentRequired,ReimbursementAttachmentDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ReimbursementAttachmentDTO initDestination() {
        return new ReimbursementAttachmentDTO();
    }

    @Override
    public void map(ReimbursementAttachmentRequired entity, ReimbursementAttachmentDTO dto) {
        super.map(entity, dto);
        dto.setId(null);
        dto.setName(entity.getName());
        dto.setOriginalFileName(null);
        dto.setAttachmentType(dictionaryEntityMapper.convert(entity.getAttachmentType()));
        dto.setRemarks(null);
        dto.setFile(null);
        dto.setMandatory(true);
        dto.setFileIncluded(false);
    }
}
