package pl.sodexo.it.gryf.web.response.publicbenefits;


import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;

import java.util.List;
//TODO uzycie encji
public class VatRegNumTrainingInstitutionExistResponse {

    //FIELDS
    private final PublicBenefitsResponseType responseType = PublicBenefitsResponseType.VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT;
    private final String message;

    private final List<TrainingInstitutionDto> trainingInstitutions;

    //CONSTRUCTORS

    public VatRegNumTrainingInstitutionExistResponse(String message, List<TrainingInstitutionDto> trainingInstitutions) {
        this.message = message;
        this.trainingInstitutions = trainingInstitutions;
    }

    //GETETRS

    public String getMessage() {
        return message;
    }

    public List<TrainingInstitutionDto> getTrainingInstitutions() {
        return trainingInstitutions;
    }

    public PublicBenefitsResponseType getResponseType() {
        return responseType;
    }
}
