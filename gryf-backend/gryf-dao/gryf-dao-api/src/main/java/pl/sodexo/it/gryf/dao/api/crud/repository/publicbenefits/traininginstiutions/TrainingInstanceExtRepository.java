package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
public interface TrainingInstanceExtRepository extends GenericRepository<TrainingInstanceExt, Long> {

    int deleteAllTrainingsInstanceExt(Long importJobId);

    // Zlicza, czy w ostatnim pliku z BUR była umowa o podanym id
    int countByIndOrderExternalId(String externalOrderId);
}
