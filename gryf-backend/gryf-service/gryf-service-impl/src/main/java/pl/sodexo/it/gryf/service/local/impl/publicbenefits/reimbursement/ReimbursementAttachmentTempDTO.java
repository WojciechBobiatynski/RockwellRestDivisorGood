package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;

/**
 * Created by jbentyn on 2016-09-30.
 */
class ReimbursementAttachmentTempDTO {

    private ReimbursementAttachment attachment;

    private ReimbursementAttachmentDTO attachmentDTO;

    public ReimbursementAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ReimbursementAttachment attachment) {
        this.attachment = attachment;
    }

    ReimbursementAttachmentDTO getAttachmentDTO() {
        return attachmentDTO;
    }

    void setAttachmentDTO(ReimbursementAttachmentDTO attachmentDTO) {
        this.attachmentDTO = attachmentDTO;
    }
}
