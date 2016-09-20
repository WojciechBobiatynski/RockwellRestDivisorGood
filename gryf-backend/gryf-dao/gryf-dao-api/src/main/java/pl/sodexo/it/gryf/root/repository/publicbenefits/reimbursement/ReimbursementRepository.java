package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementRepository extends GenericRepository<Reimbursement, Long> {

    List<Reimbursement> findReimbursements(ReimbursementSearchQueryDTO dto);
}
