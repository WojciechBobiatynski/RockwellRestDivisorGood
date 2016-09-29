package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.YesNo;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRequired;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ReimbursementTraineeAttachmentEntityMapper extends GryfEntityMapper<ReimbursementTraineeAttachment,ReimbursementTraineeAttachmentDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ReimbursementTraineeAttachmentDTO initDestination() {
        return new ReimbursementTraineeAttachmentDTO();
    }

    @Override
    public void map(ReimbursementTraineeAttachment entity, ReimbursementTraineeAttachmentDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setOriginalFileName(entity.getOriginalFileName());
        dto.setAttachmentType(dictionaryEntityMapper.convert(entity.getAttachmentType()));
        dto.setRemarks(entity.getRemarks());
        dto.setFile(null);
        dto.setMandatory(YesNo.toBoolean(entity.getRequired()));;
        dto.setFileIncluded(false);
    }
}
