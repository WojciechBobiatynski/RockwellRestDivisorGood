package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingRepository extends GenericRepository<Training, Long> {

    Training findByExternalId(String externalId);

    int deactiveTrainings(Long grantProgramId, AsynchronizeJob importJob, String modifiedUser);

    boolean isInUserInstitution(Long id, String tiUserLogin);
}
