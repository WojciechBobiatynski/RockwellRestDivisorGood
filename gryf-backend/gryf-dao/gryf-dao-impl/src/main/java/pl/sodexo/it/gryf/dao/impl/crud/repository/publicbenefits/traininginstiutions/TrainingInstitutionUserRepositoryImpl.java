package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionUserRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;

/**
 * Created by Isolution on 2017-01-24.
 */
@Repository
public class TrainingInstitutionUserRepositoryImpl extends GenericRepositoryImpl<TrainingInstitutionUser, Long> implements TrainingInstitutionUserRepository {

}
