package pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-03.
 */
public interface ReimbursementTraineeService {

    void organizeTraineeList(ReimbursementTraining training, List<ReimbursementTraineeDTO> traineeDtoList, boolean isRemoveAllowed);
}
