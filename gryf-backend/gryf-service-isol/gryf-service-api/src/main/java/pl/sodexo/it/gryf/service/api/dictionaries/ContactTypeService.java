package pl.sodexo.it.gryf.service.api.dictionaries;

import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-19.
 */
public interface ContactTypeService {

    List<ContactType> findContactTypes();

    ContactDataValidationDTO validateContractData(ContactType contactType, String value);
}
