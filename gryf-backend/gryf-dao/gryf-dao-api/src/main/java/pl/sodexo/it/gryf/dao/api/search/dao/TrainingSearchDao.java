package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingPrecalculatedDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingWithExternalIdSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingSearchDao {

    List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO params);

    List<TrainingWithExternalIdSearchResultDTO> findTrainingsWithExternalId(TrainingSearchQueryDTO dto);

    List<SimpleDictionaryDto> findTrainingCategories();

    TrainingDTO findTraining(Long trainingId);

    TrainingSearchResultDTO findTrainingDetails(Long trainingId);

    TrainingPrecalculatedDetailsDto findTrainingPrecalculatedDetails(Long trainingId, Long grantProgramId);
}
