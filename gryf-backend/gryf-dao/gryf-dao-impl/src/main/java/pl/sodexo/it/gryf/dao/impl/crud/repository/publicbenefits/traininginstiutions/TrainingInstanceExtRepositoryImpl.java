package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceExtRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
@Repository
public class TrainingInstanceExtRepositoryImpl extends GenericRepositoryImpl<TrainingInstanceExt, Long> implements TrainingInstanceExtRepository {

    @Override
    public int deleteAllTrainingsInstanceExt(Long importJobId) {
        Query query = entityManager.createNamedQuery("TrainingInstanceExt.deleteAllTrainingInstanceExt");
        query.setParameter("importJobId", importJobId);
        return query.executeUpdate();
    }

    @Override
    public int countByIndOrderExternalId(String externalOrderId) {
        String indOrderSearchString = externalOrderId.substring(0, externalOrderId.lastIndexOf("/") + 1);
        if (indOrderSearchString.isEmpty()) {
            return 0;
        }
        TypedQuery<Long> query = entityManager.createNamedQuery("TrainingInstanceExt.findByIndOrderExternalId", Long.class);
        query.setParameter("externalOrderId", indOrderSearchString);
        return query.getSingleResult().intValue();
    }

}
