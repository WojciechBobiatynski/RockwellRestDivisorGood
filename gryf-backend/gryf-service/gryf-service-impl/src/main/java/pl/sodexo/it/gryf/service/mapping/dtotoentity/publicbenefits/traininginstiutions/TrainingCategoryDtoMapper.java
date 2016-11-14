package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.GryfDtoMapper;

@Component
public class TrainingCategoryDtoMapper extends GryfDtoMapper<SimpleDictionaryDto, TrainingCategory> {

    @Override
    protected TrainingCategory initDestination() {
        return new TrainingCategory();
    }

    @Override
    protected void map(SimpleDictionaryDto dto, TrainingCategory entity) {
        super.map(dto, entity);
        entity.setId(entity.getId());
    }
}
