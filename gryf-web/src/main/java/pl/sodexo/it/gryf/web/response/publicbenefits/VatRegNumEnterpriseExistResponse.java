package pl.sodexo.it.gryf.web.response.publicbenefits;


import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;

import java.util.List;

public class VatRegNumEnterpriseExistResponse {

    //FIELDS
    private final PublicBenefitsResponseType responseType = PublicBenefitsResponseType.VAT_REG_NUM_ENTERPRISE_CONFLICT;
    private final String message;
    private final List<EnterpriseSearchResultDTO> enterprises;

    //CONSTRUCTORS

    public VatRegNumEnterpriseExistResponse(String message, List<EnterpriseSearchResultDTO> enterprises) {
        this.message = message;
        this.enterprises = enterprises;
    }

    //GETETRS

    public String getMessage() {
        return message;
    }

    public List<EnterpriseSearchResultDTO> getEnterprises() {
        return enterprises;
    }

    public PublicBenefitsResponseType getResponseType() {
        return responseType;
    }
}
