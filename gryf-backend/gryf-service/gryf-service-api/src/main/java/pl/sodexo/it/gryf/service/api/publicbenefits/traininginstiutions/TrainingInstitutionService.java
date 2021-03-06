package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface TrainingInstitutionService {

    TrainingInstitutionDto findTrainingInstitution(Long id);

    List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution);

    TrainingInstitutionSearchResultDTO findTrainingInstitutionByUserLogin(String login);

    TrainingInstitutionDto createTrainingInstitution();

    Long saveTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup);

    void updateTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup);
}
