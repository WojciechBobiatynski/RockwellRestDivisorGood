package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
@Repository
public class TrainingCategoryRepositoryImpl extends GenericRepositoryImpl<TrainingCategory, String> implements TrainingCategoryRepository {

    @Override
    public List<TrainingCategory> findByGrantProgram(Long grantProgramId) {
        TypedQuery<TrainingCategory> query = entityManager.createNamedQuery("TrainingCategory.findByGrantProgram", TrainingCategory.class);
        query.setParameter("grantProgramId", grantProgramId);
        return query.getResultList();
    }

    @Override
    public List<TrainingCategory> findByCatalogId(String catalogId) {
        TypedQuery<TrainingCategory> query = entityManager.createNamedQuery("TrainingCategory.findByCatalogId", TrainingCategory.class);
        query.setParameter("catalogId", catalogId);
        return query.getResultList();
    }

    @Override
    public List<TrainingCategory> findByIdList(List<String> idList){
        TypedQuery<TrainingCategory> query = entityManager.createNamedQuery("TrainingCategory.findByIdList", TrainingCategory.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

}
