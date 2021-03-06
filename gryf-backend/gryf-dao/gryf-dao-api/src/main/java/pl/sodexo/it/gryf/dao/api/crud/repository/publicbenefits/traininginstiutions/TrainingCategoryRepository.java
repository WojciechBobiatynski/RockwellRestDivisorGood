package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;

import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
public interface TrainingCategoryRepository extends GenericRepository<TrainingCategory, String> {
    List<TrainingCategory> findByGrantProgram(Long grantProgramId);
    List<TrainingCategory> findByCatalogId(String catalogId);
    List<TrainingCategory> findByIdList(List<String> idList);

    TrainingCategory findByGrantProgramAndName(Long grantProgramId, String name);
}
