package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.individuals;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualUserRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by Isolution on 2017-02-13.
 */
@Repository
public class IndividualUserRepositoryImpl extends GenericRepositoryImpl<IndividualUser, Long> implements IndividualUserRepository {

    @Override
    public IndividualUser findByPeselWithVerEmail(String pesel) {
        try{
            TypedQuery<IndividualUser> query = entityManager.createNamedQuery("IndividualUser.findByPeselWithVerEmail", IndividualUser.class);
            query.setParameter("pesel", pesel);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public IndividualUser findByIndividualPesel(String pesel){
        try{
            TypedQuery<IndividualUser> query = entityManager.createNamedQuery("IndividualUser.findByIndividualPesel", IndividualUser.class);
            query.setParameter("pesel", pesel);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public IndividualUser findByIndividualId(Long individualId){
        try{
            TypedQuery<IndividualUser> query = entityManager.createNamedQuery("IndividualUser.findByIndividualId", IndividualUser.class);
            query.setParameter("individualId", individualId);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

}
