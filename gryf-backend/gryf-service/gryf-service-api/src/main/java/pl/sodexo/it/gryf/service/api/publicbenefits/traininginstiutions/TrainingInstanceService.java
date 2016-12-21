package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;

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
    List<TrainingInstanceDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria);

    /**
     * Metoda która znajduje szczegółowe dane na temat instancji szkolenia
     * @param trainingInstanceId - identyfikator instancji szkolenia
     * @return szczegółowe dane na temat instancji szkolenia
     */
    TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId);

    /**
     * Metoda zwracająca listę statusów instancji szkoleń
     * @return - lista statusów
     */
    List<SimpleDictionaryDto> findTrainingInstanceStatuses();

    /**
     * Tworzy instancje szkolenia
     * @param reservationDto
     */
    void createTrainingInstance(TrainingReservationDto reservationDto);

    /**
     * Wykorzystuje instancje szkolenai (potwierdzenie pinem)
     * @param trainingId
     * @param pin
     */
    void useTrainingInstance(Long trainingId, String pin);

    /**
     * Anuluje instancje szkolenia.
     * @param trainingId
     */
    void cancelTrainingInstance(Long trainingId);

    /**
     * Metoda przesyłająca pin do szkolenia do uczestnika
     * @param trainingInstanceId
     */
    void sendReimbursmentPin(Long trainingInstanceId);

    /**
     * Metoda ponownie przesyłająca pin do szkolenia do uczestnika
     * @param trainingInstanceId
     */
    void resendReimbursmentPin(Long trainingInstanceId);
}
