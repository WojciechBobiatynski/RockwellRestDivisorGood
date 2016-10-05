package pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementsAttachmentService {

    List<ReimbursementSearchResultDTO> findReimbursementsSearchResults(ReimbursementSearchQueryDTO searchDTO);

    FileDTO getReimbursementAttachmentFile(Long attachmentId);

    FileDTO getReimbursementTraineeAttachmentFile(Long attachmentId);
}
