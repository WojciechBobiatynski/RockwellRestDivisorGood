package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementPatternDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionEntityToSearchResultMapper;

import java.util.ArrayList;
import java.util.List;

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
