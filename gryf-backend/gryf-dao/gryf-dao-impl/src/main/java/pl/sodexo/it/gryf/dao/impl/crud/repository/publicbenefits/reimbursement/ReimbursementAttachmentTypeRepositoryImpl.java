package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachmentType;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
@Repository
public class ReimbursementAttachmentTypeRepositoryImpl extends GenericRepositoryImpl<ReimbursementAttachmentType, String> implements ReimbursementAttachmentTypeRepository {
}
