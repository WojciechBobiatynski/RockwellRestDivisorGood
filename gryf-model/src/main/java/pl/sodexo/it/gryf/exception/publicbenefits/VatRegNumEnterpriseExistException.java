package pl.sodexo.it.gryf.exception.publicbenefits;

import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-10.
 */
public class VatRegNumEnterpriseExistException extends RuntimeException {

    //FIELDS

    private final List<EnterpriseSearchResultDTO> enterprises;

    //CONSTRUCTORS

    public VatRegNumEnterpriseExistException(String msg, List<EnterpriseSearchResultDTO> enterprises) {
        super(msg);
        this.enterprises = enterprises;
    }

    //GETETRS

    public List<EnterpriseSearchResultDTO> getEnterprises() {
        return enterprises;
    }
}
