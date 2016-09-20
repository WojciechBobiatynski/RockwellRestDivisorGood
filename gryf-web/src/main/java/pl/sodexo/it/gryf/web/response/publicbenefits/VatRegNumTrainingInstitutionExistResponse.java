package pl.sodexo.it.gryf.web.response.publicbenefits;


import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.List;

public class VatRegNumTrainingInstitutionExistResponse {

    //FIELDS
    private final PublicBenefitsResponseType responseType = PublicBenefitsResponseType.VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT;
    private final String message;

    private final List<TrainingInstitution> trainingInstitutions;

    //CONSTRUCTORS

    public VatRegNumTrainingInstitutionExistResponse(String message, List<TrainingInstitution> trainingInstitutions) {
        this.message = message;
        this.trainingInstitutions = trainingInstitutions;
    }

    //GETETRS

    public String getMessage() {
        return message;
    }

    public List<TrainingInstitution> getTrainingInstitutions() {
        return trainingInstitutions;
    }

    public PublicBenefitsResponseType getResponseType() {
        return responseType;
    }
}
