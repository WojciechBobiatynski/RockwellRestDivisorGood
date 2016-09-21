package pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementPatternDTO;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementPatternService {

    ReimbursementPatternDTO findReimbursementPattern(Long id);

    String findReimbursementPatternParam(Long reimbursementPatternId, String paramTypeId);
}
