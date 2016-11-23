package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;

import java.util.List;

/**
 * Created by Isolution on 2016-11-10.
 */
public interface TrainingCategoryRepository extends GenericRepository<TrainingCategory, String> {
    List<TrainingCategory> findByGrantProgram(Long grantProgramId);
}
