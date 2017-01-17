package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDataToValidateDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingInstanceSearchDao {

    /**
     * Metoda która znajduje wszystkie szkolenia do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista szkoleń do rozliczenia
     */
    List<TrainingInstanceDto> findTrainingToReimburseListByCriteria(TrainingInstanceCriteria criteria);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji szkolenia
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return szczegółowe dane na temat instancji szkolenia
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji szkolenia wraz z pinem (na potrzeby FO)
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return szczegółowe dane na temat instancji szkolenia
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetailsWithPinCode(Long trainingInstanceId);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @return - lista statusów
     */
    List<SimpleDictionaryDto>  findTiTrainingInstancesStatuses();

    /**
     * Zwraca parametry potrzbne do zwalidowania czy możemy wykonać rozliczenie dla danej instnacji szkolenia
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return dto z parametrami
     */
    TrainingInstanceDataToValidateDto findTrainingInstanceDataToValidateReimbursementCreation(Long trainingInstanceId);
}
