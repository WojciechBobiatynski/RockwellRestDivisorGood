package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
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
public interface TrainingSearchMapper {

    List<TrainingSearchResultDTO> findTrainings(@Param("criteria") UserCriteria criteria, @Param("searchParams") TrainingSearchQueryDTO trainingSearchQueryDto);

    List<TrainingWithExternalIdSearchResultDTO> findTrainingsWithExternalId(@Param("criteria") UserCriteria criteria, @Param("searchParams") TrainingSearchQueryDTO trainingSearchQueryDto);

    List<SimpleDictionaryDto> findTrainingCategories(@Param("criteria") UserCriteria criteria);

    TrainingDTO findTraining(@Param("trainingId") Long trainingId);

    TrainingSearchResultDTO findTrainingDetails(@Param("criteria") UserCriteria criteria,
                                                @Param("trainingId") Long trainingId);

    /**
     * Metoda zwracająca dto usługa na podstawie jego id oraz kryteriów użytkownika
     * @param criteria kryteria użytkownika
     * @param trainingId id usługi
     * @return dto usługi
     */
    TrainingPrecalculatedDetailsDto findTrainingPrecalculatedDetails(@Param("criteria") UserCriteria criteria,
                                                                     @Param("trainingId") Long trainingId,
                                                                     @Param("grantProgramId") Long grantProgramId);
}
