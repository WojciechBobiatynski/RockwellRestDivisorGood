package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.AuditableDtoMapper;

@Component
public class TrainingDtoMapper extends AuditableDtoMapper<TrainingDTO, Training> {

    @Autowired
    TrainingInstitutionDtoMapper institutionDtoMapper;

    @Autowired
    TrainingCategoryDtoMapper categoryDtoMapper;

    @Override
    protected Training initDestination() {
        return new Training();
    }

    @Override
    protected void map(TrainingDTO dto, Training entity) {
        super.map(dto, entity);
        entity.setTraId(dto.getTrainingId());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setPlace(dto.getPlace());
        entity.setHoursNumber(dto.getHoursNumber());
        entity.setHourPrice(dto.getHourPrice());

        if(dto.getTrainingInstitution() != null) {
            entity.setTrainingInstitution(new TrainingInstitution());
            entity.getTrainingInstitution().setId(dto.getTrainingInstitution());
        }
        if(dto.getCategory() != null) {
            entity.setCategory(new TrainingCategory());
            entity.getCategory().setCode(dto.getCategory());
        }
    }
}
