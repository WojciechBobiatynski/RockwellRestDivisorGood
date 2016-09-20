package pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
//TODO Zmiana nazwy na ReimbursementAttachmentService
public interface ReimbursementsService {

    //TODO zmiana nazwy na findReimbursementSearchResults
    List<ReimbursementSearchResultDTO> findReimbursements(ReimbursementSearchQueryDTO searchDTO);

    FileDTO getReimbursementAttachmentFile(Long attachmentId);

    FileDTO getReimbursementTraineeAttachmentFile(Long attachmentId);
}
