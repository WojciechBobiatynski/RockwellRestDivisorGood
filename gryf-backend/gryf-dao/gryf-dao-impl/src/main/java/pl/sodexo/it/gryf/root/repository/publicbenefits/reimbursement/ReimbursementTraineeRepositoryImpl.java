package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-09-16.
 */
@Repository
public class ReimbursementTraineeRepositoryImpl extends GenericRepositoryImpl<ReimbursementTrainee, Long> implements ReimbursementTraineeRepository {
}