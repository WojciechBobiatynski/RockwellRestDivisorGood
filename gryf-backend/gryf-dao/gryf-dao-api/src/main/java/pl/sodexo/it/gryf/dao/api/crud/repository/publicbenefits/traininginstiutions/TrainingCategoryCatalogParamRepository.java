package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalogParam;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
public interface TrainingCategoryCatalogParamRepository extends GenericRepository<TrainingCategoryCatalogParam, Long> {

    List<TrainingCategoryCatalogParam> findByCategoryAndGrantProgramInDate(String categoryId, Long grantProgramId, Date date);
}
