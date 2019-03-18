package pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

@Component
public class TrainingValidator {

    @Autowired
    private GryfValidator gryfValidator;

    @Value("#{'${gryf2.service.import.training.process.validatingHourPrice.excludedCategory}'.split(',')}")
    private List<String> excludedCategoriesFromValidatingHoursAndPrices;

    public void validateTraining(Training training) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(training);

        validateTrainingDates(training, violations);
        validateTrainingPrices(training, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    /**
     * Sprawdza czy kod kategorii nie podlega walidacji cen i godzin
     * @param categoryId kod sprawdzanej kategorii
     * @return true - kategoria nie podlega walidacji cen
     */
    public boolean isExcludedCategoryFromPriceValidating(String categoryId) {
        return excludedCategoriesFromValidatingHoursAndPrices.stream().anyMatch(categoryId::equals);
    }

    private void validateTrainingDates(Training training, List<EntityConstraintViolation> violations) {
        if(training.getStartDate() != null && training.getEndDate() != null && training.getEndDate().before(training.getStartDate())) {
            violations.add(new EntityConstraintViolation(Training.END_DATE_ATTR_NAME,
                    "Data zakończenia usługi nie może być wcześniejsza niż data rozpoczęcia usługi", null));
        }
    }

    private void validateTrainingPrices(Training training, List<EntityConstraintViolation> violations) {
        if(training.getCategory() != null && !isExcludedCategoryFromPriceValidating(training.getCategory().getId())) {

            if(training.getHoursNumber() == null){
                violations.add(new EntityConstraintViolation("Ilość godzin lekcyjnych nie może być pusta"));
            }
            if(training.getHourPrice() == null){
                violations.add(new EntityConstraintViolation("Cena 1h usługi nie może być pusta"));
            }
        }
    }



}
