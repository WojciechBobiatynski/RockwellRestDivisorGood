package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementTraineeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-09-16.
 */
@Repository
public class ReimbursementTraineeRepositoryImpl extends GenericRepositoryImpl<ReimbursementTrainee, Long> implements ReimbursementTraineeRepository {
}
