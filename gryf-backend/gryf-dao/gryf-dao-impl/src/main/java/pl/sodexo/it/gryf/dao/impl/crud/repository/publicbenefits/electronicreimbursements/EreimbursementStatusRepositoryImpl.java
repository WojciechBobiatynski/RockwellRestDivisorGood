package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementStatus;

/**
 * Repozytorium dla statusów rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 2016-11-18.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementStatusRepositoryImpl extends GenericRepositoryImpl<EreimbursementStatus, String> implements EreimbursementStatusRepository {

}
