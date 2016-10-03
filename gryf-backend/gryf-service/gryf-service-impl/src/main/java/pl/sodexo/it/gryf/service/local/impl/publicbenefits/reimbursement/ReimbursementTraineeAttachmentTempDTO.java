package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;

/**
 * Created by jbentyn on 2016-09-30.
 */
class ReimbursementTraineeAttachmentTempDTO {

    private ReimbursementTraineeAttachment traineeAttachment;

    private ReimbursementTraineeAttachmentDTO traineeAttachmentDTO;

    ReimbursementTraineeAttachment getTraineeAttachment() {
        return traineeAttachment;
    }

    void setTraineeAttachment(ReimbursementTraineeAttachment traineeAttachment) {
        this.traineeAttachment = traineeAttachment;
    }

    ReimbursementTraineeAttachmentDTO getTraineeAttachmentDTO() {
        return traineeAttachmentDTO;
    }

    void setTraineeAttachmentDTO(ReimbursementTraineeAttachmentDTO traineeAttachmentDTO) {
        this.traineeAttachmentDTO = traineeAttachmentDTO;
    }
}
