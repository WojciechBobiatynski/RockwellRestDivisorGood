package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalog;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Maper mapujący encję TrainingCategoryCatalog na DTO
 * Created by adziobek on 25.11.2016.
 */
@Component
public class TraningCategoryCatalogEntityMapper extends GryfEntityMapper<TrainingCategoryCatalog, SimpleDictionaryDto> {

    @Override
    protected SimpleDictionaryDto initDestination() {
        return new SimpleDictionaryDto();
    }

    @Override
    protected void map(TrainingCategoryCatalog entity, SimpleDictionaryDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setOrdinal(entity.getOrdinal());
    }
}