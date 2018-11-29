package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingcategory.TrainingCategoryDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.api.trainingCategory.TrainingCategoryService;
import pl.sodexo.it.gryf.service.api.publicbenefits.importdata.ImportTrainingValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CategoryImportTrainingValidatorImpl implements ImportTrainingValidator {

    @Autowired
    private TrainingCategoryService trainingCategoryService;

    @Override
    public List<EntityConstraintViolation> validate(GrantProgramDictionaryDTO grantProgram, ImportTrainingDTO importDTO) {
        String trainingCategory = importDTO.getCategory();
        TrainingCategoryDTO byGrantProgramAndName = trainingCategoryService.findByGrantProgramAndName((Long) grantProgram.getId(), trainingCategory);
        if (Objects.isNull(byGrantProgramAndName)) {
            List<EntityConstraintViolation> entityConstraintViolations =  new ArrayList<>();
            entityConstraintViolations.add(new EntityConstraintViolation(String.format(ENTITY_CONSTRAINT_VIOLATION_NOT_EXISTS_IN_PROGRAM, StringUtils.defaultString(trainingCategory))));
            return entityConstraintViolations;
        }
        return Collections.emptyList();
    }
}
