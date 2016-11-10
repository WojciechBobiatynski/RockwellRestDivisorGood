package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface TrainingInstitutionRepository extends GenericRepository<TrainingInstitution, Long> {

    List<TrainingInstitution> findByVatRegNum(String vatRegNum);

    TrainingInstitution getForUpdate(Long id);

    List<TrainingInstitution> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO dto);

    TrainingInstitution findTrainingInstitutionByUserLogin(String login);
}
