package pl.sodexo.it.gryf.service.local.api.dictionaries;

import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;

/**
 * Created by jbentyn on 2016-09-28.
 */
public interface ContactTypeValidator {

    ContactDataValidationDTO validateContractData(ContactType contactType, String value);
}
