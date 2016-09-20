package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementStatus;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
@Repository
public class ReimbursementStatusRepositoryImpl extends GenericRepositoryImpl<ReimbursementStatus, String> implements ReimbursementStatusRepository {
}
