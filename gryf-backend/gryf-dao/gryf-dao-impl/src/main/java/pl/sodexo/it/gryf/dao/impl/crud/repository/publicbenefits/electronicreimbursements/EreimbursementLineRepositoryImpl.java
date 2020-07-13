package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementLineRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementLine;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Repozytorium dla częściowych rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 2016-11-18.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementLineRepositoryImpl extends GenericRepositoryImpl<EreimbursementLine, Long> implements EreimbursementLineRepository {

    @Override
    public Auditable getAuditableInfoById(Long ereimbursementLineId){
        TypedQuery<Auditable> query = entityManager.createNamedQuery(EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_GET_AUDIT_BY_ID, Auditable.class);
        query.setParameter("ereimbursementLineId", ereimbursementLineId);
        List<Auditable> list = query.getResultList();
        return (!list.isEmpty()) ? list.get(0) : null;
    }

    @Override
    public List<EreimbursementLine> getListByEreimbursementId(Long ereimbursementId) {
        TypedQuery<EreimbursementLine> query = entityManager.createNamedQuery(EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_GET_LIST_BY_RMB_ID, EreimbursementLine.class);
        query.setParameter("ereimbursementId", ereimbursementId);
        return query.getResultList();
    }

    @Override
    public int deleteListByEreimbursementId(Long ereimbursementId) {
        Query query = entityManager.createNamedQuery(EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_DELETE_BY_RMB_ID);
        query.setParameter("ereimbursementId", ereimbursementId);
        return query.executeUpdate();
    }

}
