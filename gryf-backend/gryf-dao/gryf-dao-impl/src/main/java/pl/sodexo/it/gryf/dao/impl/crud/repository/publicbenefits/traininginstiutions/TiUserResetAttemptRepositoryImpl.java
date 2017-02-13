package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TiUserResetAttemptRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;

/**
 * Created by Isolution on 2017-02-13.
 */
@Repository
public class TiUserResetAttemptRepositoryImpl extends GenericRepositoryImpl<TiUserResetAttempt, String> implements TiUserResetAttemptRepository {

    @Override
    public TiUserResetAttempt findCurrentByTrainingInstitutionId(Long tiuId, Date now){
        try{
            TypedQuery<TiUserResetAttempt> query = entityManager.createNamedQuery("TiUserResetAttempt.findCurrentByTrainingInstitutionId", TiUserResetAttempt.class);
            query.setParameter("tiuId", tiuId);
            query.setParameter("now", now);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}

