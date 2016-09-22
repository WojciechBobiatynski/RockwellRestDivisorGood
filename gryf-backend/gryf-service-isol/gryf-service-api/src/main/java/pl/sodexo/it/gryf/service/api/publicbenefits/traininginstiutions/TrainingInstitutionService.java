package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface TrainingInstitutionService {

    TrainingInstitution findTrainingInstitution(Long id);

    List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution);

    TrainingInstitution createTrainingInstitution();

    TrainingInstitution saveTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup);

    void updateTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup);
}
