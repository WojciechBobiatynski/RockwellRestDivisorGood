package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceStatus;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
@Repository
public class TrainingInstanceRepositoryImpl extends GenericRepositoryImpl<TrainingInstance, Long> implements TrainingInstanceRepository {

    @Override
    public int countByTrainingAndIndividualNotCaceled(Long trainingId, Long individualId){
        TypedQuery<Long> query = entityManager.createNamedQuery("TrainingInstance.countByTrainingAndIndividualNotCaceled", Long.class);
        query.setParameter("trainingId", trainingId);
        query.setParameter("individualId", individualId);
        query.setParameter("excludedStatuses", Lists.newArrayList(TrainingInstanceStatus.CANCEL_CODE));
        return query.getSingleResult().intValue();
    }
}
