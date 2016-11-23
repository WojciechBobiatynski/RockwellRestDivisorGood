package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Maper mapujący encję TrainingCategory na SimpleDictionaryDTO
 * Created by adziobek on 23.11.2016.
 */
@Component
public class TraningCategoryEntityMapper extends GryfEntityMapper<TrainingCategory, SimpleDictionaryDto> {

    @Override
    protected SimpleDictionaryDto initDestination() {
        return new SimpleDictionaryDto();
    }

    @Override
    protected void map(TrainingCategory entity, SimpleDictionaryDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setOrdinal(entity.getOrdinal());
    }
}