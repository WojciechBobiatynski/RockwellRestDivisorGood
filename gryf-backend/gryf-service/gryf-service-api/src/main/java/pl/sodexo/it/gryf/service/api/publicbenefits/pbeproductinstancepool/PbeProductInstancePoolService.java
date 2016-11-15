package pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;

/**
 * Serwis do obsługi puli bonów użytkownika
 */
public interface PbeProductInstancePoolService {

    UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto);
}
