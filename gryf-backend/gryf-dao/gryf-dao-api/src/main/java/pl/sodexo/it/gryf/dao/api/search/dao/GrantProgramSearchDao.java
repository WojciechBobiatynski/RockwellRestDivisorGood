package pl.sodexo.it.gryf.dao.api.search.dao;

/**
 * Dao dla operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface GrantProgramSearchDao {

    /**
     * Metoda znajdująca id programu dofinansowania na podstawie id instancji usługi
     * @param trainingInstanceId - id instancji usługi
     * @return id programu dofinansowania
     */
    Long findGrantProgramIdByTrainingInstanceId(Long trainingInstanceId);

}
