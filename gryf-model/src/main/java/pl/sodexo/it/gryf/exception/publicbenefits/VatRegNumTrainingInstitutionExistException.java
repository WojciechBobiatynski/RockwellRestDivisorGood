package pl.sodexo.it.gryf.exception.publicbenefits;


import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.List;

public class VatRegNumTrainingInstitutionExistException extends RuntimeException {

    //FIELDS

    private final List<TrainingInstitution> trainingInstitutions;

    //CONSTRUCTORS

    public VatRegNumTrainingInstitutionExistException(String msg, List<TrainingInstitution> trainingInstitutions) {
        super(msg);
        this.trainingInstitutions = trainingInstitutions;
    }

    //GETETRS

    public List<TrainingInstitution> getTrainingInstitutions() {
        return trainingInstitutions;
    }

}
