package pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTrainingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-03.
 */
public interface ReimbursementTrainingService {

    void organizeTrainingList(Reimbursement reimbursement, List<ReimbursementTrainingDTO> trainingDtoList, boolean isRemoveAllowed);
}
