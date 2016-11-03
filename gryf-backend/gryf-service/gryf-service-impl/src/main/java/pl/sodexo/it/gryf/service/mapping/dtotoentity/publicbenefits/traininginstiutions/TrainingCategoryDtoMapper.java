package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingCategoryDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.GryfDtoMapper;

@Component
public class TrainingCategoryDtoMapper extends GryfDtoMapper<TrainingCategoryDto, TrainingCategory> {

    @Override
    protected TrainingCategory initDestination() {
        return new TrainingCategory();
    }

    @Override
    protected void map(TrainingCategoryDto dto, TrainingCategory entity) {
        super.map(dto, entity);
        entity.setCode(entity.getCode());
    }
}
