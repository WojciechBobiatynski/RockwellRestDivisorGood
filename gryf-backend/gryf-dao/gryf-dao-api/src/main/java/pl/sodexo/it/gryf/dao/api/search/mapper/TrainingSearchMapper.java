package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingCategoryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingSearchMapper {

    List<TrainingSearchResultDTO> findTrainings(@Param("criteria") UserCriteria criteria, @Param("searchParams") TrainingSearchQueryDTO trainingSearchQueryDto);

    List<TrainingCategoryDto> findTrainingCategories(@Param("criteria") UserCriteria criteria);

    TrainingDTO findTraining(@Param("trainingId") Long trainingId);
}
