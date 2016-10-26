package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementPatternDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementPatternEntityMapper extends GryfEntityMapper<ReimbursementPattern, ReimbursementPatternDTO> {

    @Autowired
    private ReimbursementAttachmentRequiredEntityMapper reimbursementAttachmentRequiredEntityMapper;

    @Autowired
    private ReimbursementTraineeAttachmentRequiredEntityMapper reimbursementTraineeAttachmentRequiredEntityMapper;

    @Override
    protected ReimbursementPatternDTO initDestination() {
        return new ReimbursementPatternDTO();
    }

    @Override
    public void map(ReimbursementPattern entity, ReimbursementPatternDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setReimbursementAttachmentRequiredList(reimbursementAttachmentRequiredEntityMapper.convert(entity.getReimbursementAttachmentRequiredList()));
        dto.setReimbursementTraineeAttachmentRequiredList(reimbursementTraineeAttachmentRequiredEntityMapper.convert(entity.getReimbursementTraineeAttachmentRequiredList()));
    }

    //TODO AdamK: chwilowo tak, po przeniesieniu dto do commonsów wrócę do tematu
    public void map(ReimbursementPattern entity, ReimbursementPatternDTO dto, Integer reimbursementDelay) {
        map(entity ,dto);
        dto.setReimbursementDelay(reimbursementDelay);
    }

    public ReimbursementPatternDTO convert(ReimbursementPattern source, Integer reimbursementDelay) {
        ReimbursementPatternDTO destination = initDestination();
        if (source != null) {
            map(source, destination, reimbursementDelay);
        }
        return destination;
    }

}
