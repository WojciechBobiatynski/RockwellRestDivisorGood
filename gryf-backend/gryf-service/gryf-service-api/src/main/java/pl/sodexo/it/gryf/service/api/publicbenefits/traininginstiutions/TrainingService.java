package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingService {

    TrainingDTO findTraining(Long id);

    List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO training);

    List<SimpleDictionaryDto> getTrainingCategoriesDict();

    TrainingDTO createTraining();

    Long saveTraining(TrainingDTO trainingDto);

    void updateTraining(TrainingDTO trainingDto);

    /**
     * Zwraca szkolenie na podstawie jego id oraz kontekstu zalogowanego użytkownika
     * @param id identyfikator szkolenia
     * @return dto szkolenia
     */
    TrainingSearchResultDTO findTrainingOfInstitutionById(Long id);

    /**
     * Zwraca listę z kategoriami szkoleń dla programu dofinansowania
     * @param grantProgramId
     * @return
     */
    List<SimpleDictionaryDto> findTrainingCategoriesByGrantProgram(Long grantProgramId);
}
