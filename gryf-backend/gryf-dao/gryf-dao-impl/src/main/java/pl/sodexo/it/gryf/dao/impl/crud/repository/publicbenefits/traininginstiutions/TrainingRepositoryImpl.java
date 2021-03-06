package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Repository
public class TrainingRepositoryImpl extends GenericRepositoryImpl<Training, Long> implements TrainingRepository {

    @Override
    public Training findByExternalIdAndProgramId(String externalId, Long programId){
        TypedQuery<Training> query = entityManager.createNamedQuery(Training.QUERY_FIND_BY_EXTERNAL_ID_AND_PROGRAM_ID, Training.class);
        query.setParameter("externalId", externalId);
        query.setParameter( Training.PARAMETER_GRANT_PROGRAM_ID, programId);
        List<Training> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public int deactiveTrainings(Long grantProgramId, AsynchronizeJob importJob, String modifiedUser){
        Query query = entityManager.createNamedQuery(Training.QUERY_TRAINING_DEACTIVATE_TRAININGS);
        query.setParameter("importJob", importJob);
        query.setParameter("modifiedUser", modifiedUser);
        query.setParameter( Training.PARAMETER_GRANT_PROGRAM_ID, grantProgramId);
        return query.executeUpdate();
    }

    @Override
    public boolean isInUserInstitution(Long trainingId, String tiUserLogin){
        TypedQuery<Long> query = entityManager.createNamedQuery("Training.isInUserInstitution", Long.class);
        query.setParameter("trainingId", trainingId);
        query.setParameter("tiUserLogin", tiUserLogin);
        return query.getSingleResult() > 0;
    }
}
