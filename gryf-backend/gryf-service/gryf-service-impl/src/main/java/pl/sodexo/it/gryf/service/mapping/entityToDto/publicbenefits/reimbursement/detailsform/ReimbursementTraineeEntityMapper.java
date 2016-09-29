package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.YesNo;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ReimbursementTraineeEntityMapper extends GryfEntityMapper<ReimbursementTrainee,ReimbursementTraineeDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private ReimbursementTraineeAttachmentEntityMapper reimbursementTraineeAttachmentEntityMapper;

    @Override
    protected ReimbursementTraineeDTO initDestination() {
        return new ReimbursementTraineeDTO();
    }

    @Override
    public void map(ReimbursementTrainee entity, ReimbursementTraineeDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setTraineeName(entity.getTraineeName());
        dto.setTraineeSex(dictionaryEntityMapper.convert(entity.getTraineeSex()));
        dto.setReimbursementTraineeAttachments(reimbursementTraineeAttachmentEntityMapper.convert(entity.getReimbursementTraineeAttachments()));
    }
}
