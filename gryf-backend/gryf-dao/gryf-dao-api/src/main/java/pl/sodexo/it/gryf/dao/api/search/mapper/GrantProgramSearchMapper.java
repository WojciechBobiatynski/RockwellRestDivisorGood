package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;

/**
 * Mapper dla załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface GrantProgramSearchMapper {

    /**
     * Metoda znajdująca id programu dofinansowania na podstawie id instancji szkolenia
     * @param criteria - kryteria użytkownika
     * @param trainingInstanceId - id instancji szkolenia
     * @return id programu dofinansowania
     */
    Long findGrantProgramIdByTrainingInstanceId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

}
