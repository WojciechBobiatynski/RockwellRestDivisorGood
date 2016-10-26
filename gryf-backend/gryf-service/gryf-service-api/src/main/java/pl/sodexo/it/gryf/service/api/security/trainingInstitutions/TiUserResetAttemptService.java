package pl.sodexo.it.gryf.service.api.security.trainingInstitutions;

import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;

import java.util.List;

/**
 * Serwis do operacji na żądaniach resetu hasła dla użytkownika instytucji szkoleniowej
 *
 * Created by akmiecinski on 26.10.2016.
 */
public interface TiUserResetAttemptService {

    List<TiUserResetAttemptDto> findCurrentByTrainingInstitutionId(Long tiuId);
}
