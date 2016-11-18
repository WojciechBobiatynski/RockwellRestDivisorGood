package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
public interface TrainingCategoryParamRepository extends GenericRepository<TrainingCategoryParam, Long> {

    List<TrainingCategoryParam> findByCategoryAndGrantProgramInDate(String categoryId, Long grantProgramId, Date date);
}
