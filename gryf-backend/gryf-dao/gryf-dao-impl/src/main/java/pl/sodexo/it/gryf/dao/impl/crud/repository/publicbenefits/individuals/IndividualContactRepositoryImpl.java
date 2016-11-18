package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.individuals;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by adziobek on 18.11.2016.
 */
@Repository
public class IndividualContactRepositoryImpl extends GenericRepositoryImpl<IndividualContact, Long> implements IndividualContactRepository {

    @Override
    public IndividualContact findByIndividualAndContactType(Long individualId, String contactType) {
        try {
          TypedQuery<IndividualContact> query = entityManager.createNamedQuery(IndividualContact.FIND_BY_INDIVIDUAL_AND_CONTACT_TYPE, IndividualContact.class);
          query.setParameter("individualId", individualId);
          query.setParameter("contactType", contactType);
          return query.getSingleResult();
        } catch (NoResultException exception) {
        return null;
        }
    }
}