package pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-03.
 */
public interface ReimbursementTraineeAttachmentService {

    void organizeTraineeAttachmentList(ReimbursementTrainee trainee, List<ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoList, boolean isRemoveAllowed);

}
