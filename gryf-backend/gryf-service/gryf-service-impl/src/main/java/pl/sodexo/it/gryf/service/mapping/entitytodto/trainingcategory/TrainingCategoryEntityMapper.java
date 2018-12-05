package pl.sodexo.it.gryf.service.mapping.entitytodto.trainingcategory;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingcategory.TrainingCategoryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Created by tburnicki on 28.11.2018
 */
@Component
public class TrainingCategoryEntityMapper extends GryfEntityMapper<TrainingCategory, TrainingCategoryDTO> {

    @Override
    protected TrainingCategoryDTO initDestination() {return new TrainingCategoryDTO();}

    @Override
    public void map(TrainingCategory entity, TrainingCategoryDTO dto) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
    }
}
