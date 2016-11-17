package pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;

/**
 * Serwis do obsługi puli bonów użytkownika
 */
public interface PbeProductInstancePoolService {

    //FIND METHODS

    UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto);

    //ACTION METHODS

    void createProductInstancePool(Long orderId);

    void createTrainingInstance(Long trainingId, Long individualId, Long grantProgramId, Integer toReservedNum);

    void useTrainingInstance(Long trainingId, String pin);

    void cancelTrainingInstance(Long trainingId);
}
