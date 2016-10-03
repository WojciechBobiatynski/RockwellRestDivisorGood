package pl.sodexo.it.gryf.common.exception.publicbenefits;


import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;

import java.util.List;

public class VatRegNumTrainingInstitutionExistException extends RuntimeException {

    //FIELDS

    private final List<TrainingInstitutionDto> trainingInstitutions;

    //CONSTRUCTORS

    public VatRegNumTrainingInstitutionExistException(String msg, List<TrainingInstitutionDto> trainingInstitutions) {
        super(msg);
        this.trainingInstitutions = trainingInstitutions;
    }

    //GETETRS

    public List<TrainingInstitutionDto> getTrainingInstitutions() {
        return trainingInstitutions;
    }

}
