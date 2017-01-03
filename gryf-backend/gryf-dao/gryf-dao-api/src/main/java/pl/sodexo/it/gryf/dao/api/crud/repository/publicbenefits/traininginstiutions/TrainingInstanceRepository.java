package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
public interface TrainingInstanceRepository extends GenericRepository<TrainingInstance, Long> {

    int countByTrainingAndIndividualNotCaceled(Long trainingId, Long individualId);

    boolean isInUserInstitution(Long trainingInstanceId, String tiUserLogin);

    boolean isInUserIndividual(Long trainingInstanceId, String indUserLogin);

    List<TrainingInstance> findByExternalIdAndPesel(String externalId, String pesel);
}
