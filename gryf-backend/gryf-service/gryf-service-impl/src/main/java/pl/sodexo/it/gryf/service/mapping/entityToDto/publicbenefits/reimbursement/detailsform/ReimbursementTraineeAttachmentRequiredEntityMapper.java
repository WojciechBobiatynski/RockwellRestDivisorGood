package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRequired;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ReimbursementTraineeAttachmentRequiredEntityMapper extends GryfEntityMapper<ReimbursementTraineeAttachmentRequired,ReimbursementTraineeAttachmentDTO>{

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ReimbursementTraineeAttachmentDTO initDestination() {
        return new ReimbursementTraineeAttachmentDTO();
    }

    @Override
    public void map(ReimbursementTraineeAttachmentRequired entity, ReimbursementTraineeAttachmentDTO dto) {
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
