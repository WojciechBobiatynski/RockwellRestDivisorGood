package pl.sodexo.it.gryf.service.local.api.dictionaries;

import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;

/**
 * Created by jbentyn on 2016-09-28.
 */
public interface ContactTypeValidationService {

    ContactDataValidationDTO validateContractData(ContactType contactType, String value);
}
