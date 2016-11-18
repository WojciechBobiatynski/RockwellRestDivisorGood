package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryParamRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
@Repository
class TrainingCategoryParamRepositoryImpl extends GenericRepositoryImpl<TrainingCategoryParam, Long> implements TrainingCategoryParamRepository {

    @Override
    public List<TrainingCategoryParam> findByCategoryAndGrantProgramInDate(String categoryId, Long grantProgramId, Date date){
        TypedQuery<TrainingCategoryParam> query = entityManager.createNamedQuery("TrainingCategoryParam.findByCategoryAndGrantProgramInDate", TrainingCategoryParam.class);
        query.setParameter("categoryId", categoryId);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
