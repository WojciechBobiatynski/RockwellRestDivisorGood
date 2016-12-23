package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;

/**
 * Repozytorium dla załączników do rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
public interface EreimbursementAttachmentRepository extends GenericRepository<ErmbsAttachment, Long>{

    boolean isInLoggedUserInstitution(Long ereimbursementAttachmentId, String tiUserLogin);
}
