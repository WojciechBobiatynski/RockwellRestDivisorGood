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
    public Training findByExternalId(String externalId){
        TypedQuery<Training> query = entityManager.createNamedQuery("Training.findByExternalId", Training.class);
        query.setParameter("externalId", externalId);
        List<Training> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public int deactiveTrainings(AsynchronizeJob importJob, String modifiedUser){
        Query query = entityManager.createNamedQuery("Training.deactiveTrainings");
        query.setParameter("importJob", importJob);
        query.setParameter("modifiedUser", modifiedUser);
        return query.executeUpdate();
    }
}
