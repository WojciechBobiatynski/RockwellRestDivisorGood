package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryCatalogParamRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalogParam;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
@Repository
class TrainingCategoryCatalogParamRepositoryImpl extends GenericRepositoryImpl<TrainingCategoryCatalogParam, Long> implements TrainingCategoryCatalogParamRepository {

    @Override
    public List<TrainingCategoryCatalogParam> findByCategoryAndGrantProgramInDate(String categoryId, Long grantProgramId, Date date){
        TypedQuery<TrainingCategoryCatalogParam> query = entityManager.createNamedQuery("TrainingCategoryCatalogParam.findByCategoryAndGrantProgramInDate", TrainingCategoryCatalogParam.class);
        query.setParameter("categoryId", categoryId);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
