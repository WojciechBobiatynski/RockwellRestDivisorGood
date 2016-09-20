package pl.sodexo.it.gryf.root.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface TrainingInstitutionRepository extends GenericRepository<TrainingInstitution, Long> {

    List<TrainingInstitution> findByVatRegNum(String vatRegNum);

    TrainingInstitution getForUpdate(Long id);

    List<TrainingInstitution> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO dto);
}
