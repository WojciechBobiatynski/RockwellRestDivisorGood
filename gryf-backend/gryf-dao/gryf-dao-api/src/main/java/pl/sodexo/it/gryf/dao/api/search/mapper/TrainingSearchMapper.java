package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
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
public interface TrainingSearchMapper {

    List<TrainingSearchResultDTO> findTrainings(@Param("criteria") UserCriteria criteria, @Param("searchParams") TrainingSearchQueryDTO trainingSearchQueryDto);

    List<SimpleDictionaryDto> findTrainingCategories(@Param("criteria") UserCriteria criteria);

    TrainingDTO findTraining(@Param("trainingId") Long trainingId);

    /**
     * Metoda która znajduje wszystkie szkolenia do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista szkoleń do rozliczenia
     */
    List<TrainingToReimburseDto> findTrainingToReimburseListByCriteria(@Param("criteria") TrainingToReimburseCriteria criteria);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @param criteria kryteria użytkownika
     * @return - lista statusów
     */
    List<SimpleDictionaryDto>  findTiTrainingInstancesStatuses(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda zwracająca dto szkolenie na podstawie jego id oraz kryteriów użytkownika
     * @param criteria kryteria użytkownika
     * @param trainingId id szkolenia
     * @return dto szkolenia
     */
    TrainingSearchResultDTO findTrainingOfInstitutionById(@Param("criteria") UserCriteria criteria, @Param("trainingId") Long trainingId);
}
