package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-09-03.
 */
@Repository
public class ReimbursementDeliveryStatusRepositoryImpl extends GenericRepositoryImpl<ReimbursementDeliveryStatus, String> implements ReimbursementDeliveryStatusRepository {
}
