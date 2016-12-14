package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalog;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

@Component
public class TrainingDtoMapper extends VersionableDtoMapper<TrainingDTO, Training> {

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Override
    protected Training initDestination() {
        return new Training();
    }

    @Override
    protected void map(TrainingDTO dto, Training entity) {
        super.map(dto, entity);
        entity.setId(dto.getTrainingId());
        entity.setExternalId(dto.getExternalTrainingId());
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
            entity.getCategory().setId(dto.getCategory());
            if (dto.getTrainingCategoryCatalogId() != null) {
                entity.getCategory().setTrainingCategoryCatalog(new TrainingCategoryCatalog());
                entity.getCategory().getTrainingCategoryCatalog().setId(dto.getTrainingCategoryCatalogId());
            }
        }
        entity.setReimbursmentConditions(dto.getReimbursmentConditions());

        entity.setActive(dto.isActive());
        entity.setDeactivateUser(dto.getDeactivateUser());
        entity.setDeactivateDate(dto.getDeactivateDate());
        entity.setDeactivateJob(dto.getDeactivateJobId() != null ? asynchronizeJobRepository.get(dto.getDeactivateJobId()) : null);

    }
}
