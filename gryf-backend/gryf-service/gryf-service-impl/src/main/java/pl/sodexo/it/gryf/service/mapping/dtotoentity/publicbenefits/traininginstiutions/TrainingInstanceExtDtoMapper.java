package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

@Component
public class TrainingInstanceExtDtoMapper extends VersionableDtoMapper<TrainingInstanceExtDTO, TrainingInstanceExt> {

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Override
    protected TrainingInstanceExt initDestination() {
        return new TrainingInstanceExt();
    }

    @Override
    protected void map(TrainingInstanceExtDTO dto, TrainingInstanceExt entity) {
        super.map(dto, entity);

        if (dto.getTrainingInstitution() != null) {
            entity.setTrainingInstitution(new TrainingInstitution());
            entity.getTrainingInstitution().setId(dto.getTrainingInstitution());
        }

        entity.setVatRegNum(dto.getVatRegNum());
        entity.setName(dto.getName());
        entity.setTrainingExternalId(dto.getTrainingExternalId());
        entity.setTrainingName(dto.getTrainingName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setPlace(dto.getPlace());
        entity.setPrice(dto.getPrice());
        entity.setHoursNumber(dto.getHoursNumber());
        entity.setHourPrice(dto.getHourPrice());
        if (dto.getCategory() != null) {
            entity.setCategory(new TrainingCategory());
            entity.getCategory().setId(dto.getCategory());
        }
        entity.setCertificateRemark(dto.getCertificateRemark());
        entity.setIndOrderExternalId(dto.getIndOrderExternalId());
    }
}
