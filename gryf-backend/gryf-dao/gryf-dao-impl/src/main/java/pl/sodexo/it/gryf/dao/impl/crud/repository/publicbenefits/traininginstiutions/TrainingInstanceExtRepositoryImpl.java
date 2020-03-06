package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceExtRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
@Repository
public class TrainingInstanceExtRepositoryImpl extends GenericRepositoryImpl<TrainingInstanceExt, Long> implements TrainingInstanceExtRepository {

    @Override
    public int deleteAllTrainingsInstanceExt(Long grantProgramId, Long importJobId) {
        Query query = entityManager.createNamedQuery(TrainingInstanceExt.QUERY_TRAINING_INSTANCE_EXT_DELETE_ALL_TRAINING_INSTANCE_EXT);
        query.setParameter("importJobId", importJobId);
        query.setParameter(TrainingInstanceExt.PARAMETER_GRANT_PROGRAM_ID, grantProgramId);
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

    @Override
    public boolean isIndOrderExternalIdAndTrainingExternalId(String contractId, String trainingExternalId) {
        if (isNotEmpty(contractId) && isNotEmpty(trainingExternalId)) {
            return !getTrainingInstanceExts(contractId, trainingExternalId).isEmpty();
        } else {
            return false;
        }
    }

    private List<TrainingInstanceExt> getTrainingInstanceExts(String contractId, String trainingExternalId) {
        String indOrderSearchString = String.join("", "/", contractId, "/");
        TypedQuery<TrainingInstanceExt> query = entityManager.createNamedQuery(TrainingInstanceExt.QUERY_TRAINING_INSTANCE_EXT_FIND_ORDER_WITH_TRAINING, TrainingInstanceExt.class);
        query.setParameter("externalOrderId", indOrderSearchString);
        query.setParameter("trainingExternalId", trainingExternalId);
        return query.getResultList();
    }

    private boolean isNotEmpty(String string) {
        return (string != null && !string.trim().isEmpty());
    }

}
