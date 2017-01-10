package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDataToValidateDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;

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
    List<TrainingInstanceDto> findTrainingToReimburseListByCriteria(@Param("criteria") TrainingInstanceCriteria criteria);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji szkolenia
     * @param criteria - kryteria użytkownika
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return szczegółowe dane na temat instancji szkolenia
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetails(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @param criteria kryteria użytkownika
     * @return - lista statusów
     */
    List<SimpleDictionaryDto>  findTiTrainingInstancesStatuses(@Param("criteria") UserCriteria criteria);

    /**
     * Zwraca parametry potrzbne do zwalidowania czy możemy wykonać rozliczenie dla danej instnacji szkolenia
     * @param criteria kryteria użytkownika
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return dto z parametrami
     */
    TrainingInstanceDataToValidateDto findTrainingInstanceDataToValidateReimbursementCreation(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);
}
