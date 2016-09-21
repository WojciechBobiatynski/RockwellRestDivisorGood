package pl.sodexo.it.gryf.web.response.publicbenefits;


import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;

import java.util.List;

public class PeselIndividualExistResponse {

    //FIELDS
    private final PublicBenefitsResponseType responseType = PublicBenefitsResponseType.PESEL_INDIVIDUAL_CONFLICT;
    private final String message;
    private final List<IndividualSearchResultDTO> individuals;

    //CONSTRUCTORS

    public PeselIndividualExistResponse(String message, List<IndividualSearchResultDTO> individuals) {
        this.message = message;
        this.individuals = individuals;
    }

    //GETETRS

    public String getMessage() {
        return message;
    }

    public List<IndividualSearchResultDTO> getIndividuals() {
        return individuals;
    }

    public PublicBenefitsResponseType getResponseType() {
        return responseType;
    }
}
