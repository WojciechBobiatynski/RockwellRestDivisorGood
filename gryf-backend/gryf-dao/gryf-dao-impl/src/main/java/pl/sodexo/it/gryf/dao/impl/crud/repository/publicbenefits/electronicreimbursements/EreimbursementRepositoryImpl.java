package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;

import javax.persistence.TypedQuery;

/**
 * Repozytorium dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 2016-11-18.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementRepositoryImpl extends GenericRepositoryImpl<Ereimbursement, Long> implements EreimbursementRepository {

    @Override
    public boolean isInLoggedUserInstitution(Long ereimbursementId, String tiUserLogin){
        TypedQuery<Long> query = entityManager.createNamedQuery("Ereimbursement.isInUserInstitution", Long.class);
        query.setParameter("ereimbursementId", ereimbursementId);
        query.setParameter("tiUserLogin", tiUserLogin);
        return query.getSingleResult() > 0;
    }

}
