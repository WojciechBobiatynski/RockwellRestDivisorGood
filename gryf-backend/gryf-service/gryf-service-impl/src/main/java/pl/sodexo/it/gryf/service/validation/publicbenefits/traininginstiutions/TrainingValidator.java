package pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

@Component
public class TrainingValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateTraining(Training training) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(training);

        validateTrainingDates(training, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    private void validateTrainingDates(Training training, List<EntityConstraintViolation> violations) {
        if(training.getStartDate() != null && training.getEndDate() != null && training.getEndDate().before(training.getStartDate())) {
            violations.add(new EntityConstraintViolation(Training.END_DATE_ATTR_NAME,
                    "Data zakończenia szkolenia nie może być wcześniejsza niż data rozpoczęcia szkolenia", null));
        }
    }
}
