package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionUserRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by Isolution on 2017-01-24.
 */
@Repository
public class TrainingInstitutionUserRepositoryImpl extends GenericRepositoryImpl<TrainingInstitutionUser, Long> implements TrainingInstitutionUserRepository {

    @Override
    public TrainingInstitutionUser findByLoginIgnoreCase(String login){
        try{
            TypedQuery<TrainingInstitutionUser> query = entityManager.createNamedQuery("TrainingInstitutionUser.findByLoginIgnoreCase", TrainingInstitutionUser.class);
            query.setParameter("login", login);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public TrainingInstitutionUser findByEmailIgnoreCase(String email){
        try{
            TypedQuery<TrainingInstitutionUser> query = entityManager.createNamedQuery("TrainingInstitutionUser.findByEmailIgnoreCase", TrainingInstitutionUser.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}
