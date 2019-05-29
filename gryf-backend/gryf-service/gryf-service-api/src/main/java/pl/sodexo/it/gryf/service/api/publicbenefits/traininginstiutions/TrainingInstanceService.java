package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceUseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingInstanceService {

    /**
     * Metoda która znajduje wszystkie usługi do rozliczenia na podstawie wybranych kryteriów wyszkuwiania
     * @param criteria - kryteria wyszukiwania
     * @return lista usług do rozliczenia
     */
    List<TrainingInstanceDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria);

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
    List<SimpleDictionaryDto> findTrainingInstanceStatuses();

    /**
     * Tworzy instancje usługi
     * @param reservationDto
     */
    void createTrainingInstance(TrainingReservationDto reservationDto);

    /**
     * Wykorzystuje instancje szkolenai (potwierdzenie pinem)
     * @param useDto
     */
    void useTrainingInstance(TrainingInstanceUseDto useDto);

    /**
     * Anuluje instancje usługi.
     * @param trainingInstanceId
     * @param version
     */
    void cancelTrainingInstance(Long trainingInstanceId, Integer version);

    /**
     * Metoda przesyłająca pin do usługi do uczestnika
     * @param trainingInstanceId
     */
    void sendReimbursmentPin(Long trainingInstanceId);

    /**
     * Metoda ponownie przesyłająca pin do usługi do uczestnika
     * @param trainingInstanceId
     */
    void resendReimbursmentPin(Long trainingInstanceId);

    /**
     * Zmienia liczbę zarezerwowanych bonów dla odbytego szkolenia
     * @param useDto
     */
    void reduceProductAssignedNum(TrainingInstanceUseDto useDto);

    /**
     * Anuluje rozliczenie i zmienia status instancji usługi na odbyte.
     * @param trainingInstanceId
     * @param version
     */
    void cancelTrainingReimbursement(Long trainingInstanceId, Integer version);

    /**
     * Anuluje szkolenie. Instancja zmienia status z Odbyte na Anulowane.
     * @param trainingInstanceId
     * @param version
     */
    void cancelTrainingInstanceDone(Long trainingInstanceId, Integer version);

    /**
     * Odrzuca rozliczenie szkolenia. Instancja zmienia status z Rozliczone na Nie rozliczone. Rozliczenie zmienai status z Rozliczone na Odrzucone.
     * @param trainingInstanceId
     * @param version
     */
    void rejectTrainingInstanceReimb(Long trainingInstanceId, Integer version);

    /**
     * uaktualnai znacznik czy została dokonana ocena usługi
     * @param externalId
     * @param pesel
     * @param opinionDone
     * @return
     */
    Long updateOpinionDone(String externalId, String pesel, boolean opinionDone);

    /**
     * Sprawdza czy dana instancja usługi jest w obrebie usługodawcy skolenia użytkownika zalogwanego.
     * @param trainingInstanceId instancja usługi
     * @return
     */
    boolean isTrainingInstanceInLoggedUserInstitution(Long trainingInstanceId);

    /**
     * Sprawdza czy dana instancja usługi jest w obrebie danego użytkownika indywidualnego.
     * @param trainingInstanceId instancja usługi
     * @return
     */
    boolean isTrainingInstanceInLoggedIndividual(Long trainingInstanceId);

    /**
     * Znajduje od jakiej daty mozna rezerwować szklenie
     * @param grantProgramId identyfikator programu
     * @param date data
     * @return
     */
    Date findReservationDatePossibility(Long grantProgramId, Date date);
}
