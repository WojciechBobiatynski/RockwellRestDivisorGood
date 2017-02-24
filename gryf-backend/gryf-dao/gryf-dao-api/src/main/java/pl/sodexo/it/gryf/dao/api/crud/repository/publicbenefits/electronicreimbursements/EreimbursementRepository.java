package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;

/**
 * Repozytorium dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 2016-11-18.
 */
public interface EreimbursementRepository extends GenericRepository<Ereimbursement, Long> {

    boolean isInLoggedUserInstitution(Long ereimbursementId, String tiUserLogin);

    Auditable getAuditableInfoById(Long ereimbursementId);
}
