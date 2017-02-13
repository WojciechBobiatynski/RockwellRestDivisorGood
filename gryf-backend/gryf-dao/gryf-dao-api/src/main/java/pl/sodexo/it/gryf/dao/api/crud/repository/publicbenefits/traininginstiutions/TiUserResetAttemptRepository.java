package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;

import java.util.Date;

/**
 * Created by Isolution on 2017-02-13.
 */
public interface TiUserResetAttemptRepository extends GenericRepository<TiUserResetAttempt, String> {

    TiUserResetAttempt findCurrentByTrainingInstitutionId(Long tiuId, Date now);
}
