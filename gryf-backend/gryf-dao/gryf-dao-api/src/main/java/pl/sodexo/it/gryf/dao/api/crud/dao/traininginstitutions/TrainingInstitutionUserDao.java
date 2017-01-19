package pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;

/**
 * Dao dla uzytkowników instytucji szkoleniowej
 *
 * Created by jbentyn on 2016-10-06.
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface TrainingInstitutionUserDao extends JpaRepository<TrainingInstitutionUser, Long>{

    TrainingInstitutionUser findByLoginIgnoreCase(String login);

    TrainingInstitutionUser findByEmailIgnoreCase(String email);
}
