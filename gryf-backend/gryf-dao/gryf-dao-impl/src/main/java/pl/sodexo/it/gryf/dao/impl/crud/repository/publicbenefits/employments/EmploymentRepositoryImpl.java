package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.employments;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.employments.EmploymentRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by adziobek on 08.11.2016.
 */
@Repository
public class EmploymentRepositoryImpl extends GenericRepositoryImpl<Employment, Long> implements EmploymentRepository {

    @Override
    public Employment findByIndividualIdAndEnterpriseId(Long individualId, Long enterpriseId) {
        try {
            TypedQuery<Employment> query = entityManager.createNamedQuery("Employment.findByIndividualAndEnterpriseId", Employment.class);
            query.setParameter("individualId", individualId);
            query.setParameter("enterpriseId", enterpriseId);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}