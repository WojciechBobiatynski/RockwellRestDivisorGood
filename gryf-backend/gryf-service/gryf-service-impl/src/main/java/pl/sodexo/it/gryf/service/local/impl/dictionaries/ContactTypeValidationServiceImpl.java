package pl.sodexo.it.gryf.service.local.impl.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.service.local.api.dictionaries.validation.ContactDataValidator;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidationService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

/**
 * Created by jbentyn on 2016-09-28.
 */
@Service
public class ContactTypeValidationServiceImpl implements ContactTypeValidationService {

    @Autowired
    private ApplicationContext context;

    @Override
    public ContactDataValidationDTO validateContractData(ContactType contactType, String value) {
        ContactDataValidationDTO result = new ContactDataValidationDTO();

        if (contactType != null && !StringUtils.isEmpty(contactType.getValidationClass())) {
            ContactDataValidator validator = createValidator(contactType.getValidationClass());
            if (!validator.validate(value)) {
                result.setValid(false);
                result.setMessage(validator.createMessage(value));
            }
        }
        return result;
    }

    //PRIVATE METHODS

    private ContactDataValidator createValidator(String validationClass) {
        return (ContactDataValidator) BeanUtils.findBean(context, validationClass);
    }

//    private RuntimeException createValidatorCreateException(String validationClass, Exception e) {
//        return new RuntimeException(String.format("Nie udało się utworzyć klasy walidujacej kontakt na podstawie wartości %s - błąd: ", validationClass, e.getMessage()), e);
//    }
}
