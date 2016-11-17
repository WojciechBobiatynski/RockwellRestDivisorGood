package pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;

/**
 * Serwis do obsługi puli bonów użytkownika
 */
public interface PbeProductInstancePoolService {

    //FIND METHODS

    UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto);

    //ACTION METHODS

    void createProductInstancePool(Long orderId);

    void createTrainingInstance(TrainingReservationDto reservationDto);

    void useTrainingInstance(Long trainingId, String pin);

    void cancelTrainingInstance(Long trainingId);
}
