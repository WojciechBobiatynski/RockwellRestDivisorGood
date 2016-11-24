package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

/**
 * Created by Isolution on 2016-11-07.
 */
public interface TrainingInstanceRepository extends GenericRepository<TrainingInstance, Long> {

    int countByTrainingAndIndividualNotCaceled(Long trainingId, Long individualId);
}
