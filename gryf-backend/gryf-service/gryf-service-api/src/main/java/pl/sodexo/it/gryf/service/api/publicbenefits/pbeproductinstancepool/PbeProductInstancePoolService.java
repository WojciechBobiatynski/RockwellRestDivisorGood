package pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.ContractPbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;

import java.util.List;

/**
 * Serwis do obsługi puli bonów użytkownika
 */
public interface PbeProductInstancePoolService {

    UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto);

    /**
     * Znajduje pule bonów dla danej umowy
     * @param contractId - id umowy
     * @return lista puli bonów
     */
    List<ContractPbeProductInstancePoolDto> findPoolInstancesByContractId(Long contractId);

}
