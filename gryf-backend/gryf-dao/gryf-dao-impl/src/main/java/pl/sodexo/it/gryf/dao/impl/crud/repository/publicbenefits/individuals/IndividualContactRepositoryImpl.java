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
    public IndividualContact findByIndividualAndContactType(Long individualId, String contactTypeType) {
        try {
          TypedQuery<IndividualContact> query = entityManager.createNamedQuery("IndividualContact.findByIndividualAndContactType", IndividualContact.class);
          query.setParameter("individualId", individualId);
          query.setParameter("contactTypeType", contactTypeType);
          return query.getSingleResult();
        } catch (NoResultException exception) {
        return null;
        }
    }
}