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
     * Metoda która znajduje wszystkie usługi do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista usług do rozliczenia
     */
    List<TrainingInstanceDto> findTrainingToReimburseListByCriteria(TrainingInstanceCriteria criteria);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji usługi
     * @param trainingInstanceId - identyfikator instancji usługi
     * @return szczegółowe dane na temat instancji usługi
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji usługi wraz z pinem (na potrzeby FO)
     * @param trainingInstanceId - identyfikator instancji usługi
     * @return szczegółowe dane na temat instancji usługi
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetailsWithPinCode(Long trainingInstanceId);

    /**
     * Metoda zwracająca listę statusów instancji usług
     * @return - lista statusów
     */
    List<SimpleDictionaryDto>  findTiTrainingInstancesStatuses();

    /**
     * Zwraca parametry potrzbne do zwalidowania czy możemy wykonać rozliczenie dla danej instnacji usługi
     * @param trainingInstanceId - identyfikator instancji usługi
     * @return dto z parametrami
     */
    TrainingInstanceDataToValidateDto findTrainingInstanceDataToValidateReimbursementCreation(Long trainingInstanceId);
}
