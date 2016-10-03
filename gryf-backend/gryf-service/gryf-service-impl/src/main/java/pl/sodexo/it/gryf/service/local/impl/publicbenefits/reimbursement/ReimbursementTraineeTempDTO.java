package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;

/**
 * Created by jbentyn on 2016-09-30.
 */
class ReimbursementTraineeTempDTO {

    private ReimbursementTrainee trainee;

    private ReimbursementTraineeDTO traineeDTO;

    ReimbursementTrainee getTrainee() {
        return trainee;
    }

    void setTrainee(ReimbursementTrainee trainee) {
        this.trainee = trainee;
    }

    ReimbursementTraineeDTO getTraineeDTO() {
        return traineeDTO;
    }

    void setTraineeDTO(ReimbursementTraineeDTO traineeDTO) {
        this.traineeDTO = traineeDTO;
    }
}
