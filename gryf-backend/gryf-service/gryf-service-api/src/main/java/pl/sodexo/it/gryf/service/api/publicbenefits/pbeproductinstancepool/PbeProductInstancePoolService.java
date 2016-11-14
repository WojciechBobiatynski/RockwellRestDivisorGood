package pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool;

import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;

import java.util.List;

/**
 * Serwis do obsługi puli bonów użytkownika
 */
public interface PbeProductInstancePoolService {

    List<PbeProductInstancePoolDto> findProductInstancePoolsOfUser(IndUserAuthDataDto userAuthDataDto);
}
