package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.criteria.trainingtoreimburse.TrainingToReimburseCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.trainingtoreimburse.TrainingToReimburseDto;

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
     * Metoda która znajduje wszystkie szkolenia do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista szkoleń do rozliczenia
     */
    List<TrainingToReimburseDto> findTrainingToReimburseListByCriteria(TrainingToReimburseCriteria criteria);
}
