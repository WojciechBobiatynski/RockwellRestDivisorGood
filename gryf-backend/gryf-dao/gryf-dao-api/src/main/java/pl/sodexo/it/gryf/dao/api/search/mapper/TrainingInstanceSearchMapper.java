package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstancesDto;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingInstanceSearchMapper {

    /**
     * Metoda która znajduje wszystkie szkolenia do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista szkoleń do rozliczenia
     */
    List<TrainingInstancesDto> findTrainingToReimburseListByCriteria(@Param("criteria") TrainingInstanceCriteria criteria);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @param criteria kryteria użytkownika
     * @return - lista statusów
     */
    List<SimpleDictionaryDto>  findTiTrainingInstancesStatuses(@Param("criteria") UserCriteria criteria);
}
