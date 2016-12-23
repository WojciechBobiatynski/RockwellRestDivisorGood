package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;

import javax.persistence.TypedQuery;

/**
 * Repozytorium dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 2016-11-18.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementAttachmentRepositoryImpl extends GenericRepositoryImpl<ErmbsAttachment, Long> implements EreimbursementAttachmentRepository {

    @Override
    public boolean isInLoggedUserInstitution(Long ereimbursementAttachmentId, String tiUserLogin){
        TypedQuery<Long> query = entityManager.createNamedQuery("ErmbsAttachment.isInUserInstitution", Long.class);
        query.setParameter("ereimbursementAttachmentId", ereimbursementAttachmentId);
        query.setParameter("tiUserLogin", tiUserLogin);
        return query.getSingleResult() > 0;
    }
}
