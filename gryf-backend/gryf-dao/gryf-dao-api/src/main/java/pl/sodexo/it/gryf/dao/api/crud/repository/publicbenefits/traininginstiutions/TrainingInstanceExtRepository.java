package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
public interface TrainingInstanceExtRepository extends GenericRepository<TrainingInstanceExt, Long> {

    int deleteAllTrainingsInstanceExt(Long grantProgramId, Long importJobId);

    // Zlicza, czy w ostatnim pliku z BUR była umowa o podanym id
    int countByIndOrderExternalId(String externalOrderId);

    /**
     * Checks if the participant exists in the BUR training file
     * @param contractId participant contract id
     * @param trainingExternalId external training id
     * @return count of exist data rows
     */
    boolean isIndOrderExternalIdAndTrainingExternalId(String contractId, String trainingExternalId);
}
