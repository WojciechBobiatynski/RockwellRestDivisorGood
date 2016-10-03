package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTrainingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;

/**
 * Created by jbentyn on 2016-09-30.
 */
class ReimbursementTrainingTempDTO {

    private ReimbursementTraining training;

    private ReimbursementTrainingDTO trainingDTO;

    ReimbursementTraining getTraining() {
        return training;
    }

    void setTraining(ReimbursementTraining training) {
        this.training = training;
    }

    ReimbursementTrainingDTO getTrainingDTO() {
        return trainingDTO;
    }

    void setTrainingDTO(ReimbursementTrainingDTO trainingDTO) {
        this.trainingDTO = trainingDTO;
    }
}
