package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentRepository;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
@Repository
public class ReimbursementAttachmentRepositoryImpl extends GenericRepositoryImpl<ReimbursementAttachment, Long> implements ReimbursementAttachmentRepository {
}
