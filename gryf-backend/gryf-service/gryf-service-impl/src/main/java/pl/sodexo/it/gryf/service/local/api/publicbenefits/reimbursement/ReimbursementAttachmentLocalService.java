package pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-03.
 */
public interface ReimbursementAttachmentLocalService {

    String findAttachmentName(Reimbursement reimbursement, String attachmentType, Long attachmentId, String attachmentName);

    void organizeAttachmentList(Reimbursement reimbursement, List<ReimbursementAttachmentDTO> attachmentDtoList, boolean isRemoveAllowed);
}
