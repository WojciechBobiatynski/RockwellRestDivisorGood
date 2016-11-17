package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstancesDto;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingInstanceService {

    /**
     * Metoda która znajduje wszystkie szkolenia do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista szkoleń do rozliczenia
     */
    List<TrainingInstancesDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @return - lista statusów
     */
    List<SimpleDictionaryDto> findTrainingInstanceStatuses();
}
