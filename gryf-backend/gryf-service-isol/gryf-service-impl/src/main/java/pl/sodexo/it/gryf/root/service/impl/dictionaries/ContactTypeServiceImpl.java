package pl.sodexo.it.gryf.root.service.impl.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.common.validation.dictionaries.ContactDataValidator;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.root.repository.dictionaries.ContactTypeRepository;
import pl.sodexo.it.gryf.root.service.dictionaries.ContactTypeService;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-19.
 */
@Service
@Transactional
public class ContactTypeServiceImpl implements ContactTypeService {

    //PRIVATE FIELDS

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    //PUBLIC METHODS

    @Override public List<ContactType> findContactTypes(){
        return contactTypeRepository.findAll();
    }

    @Override public ContactDataValidationDTO validateContractData(ContactType contactType, String value){
        ContactDataValidationDTO result = new ContactDataValidationDTO();

        if(contactType != null && !StringUtils.isEmpty(contactType.getValidationClass())){
            ContactDataValidator validator = createValidator(contactType.getValidationClass());
            if(!validator.validate(value)){
                result.setValid(false);
                result.setMessage(validator.createMessage(value));
            }
        }
        return result;
    }

    //PRIVATE METHODS

    private ContactDataValidator createValidator(String validationClass){
        ContactDataValidator validator;
        try {
            validator = (ContactDataValidator)Class.forName(validationClass).newInstance();
        } catch (InstantiationException e) {
            throw createValidatorCreateException(validationClass, e);
        } catch (IllegalAccessException e) {
            throw createValidatorCreateException(validationClass, e);
        } catch (ClassNotFoundException e) {
            throw createValidatorCreateException(validationClass, e);
        }
        return validator;
    }

    private RuntimeException createValidatorCreateException(String validationClass, Exception e){
        return new RuntimeException(String.format("Nie udało się utworzyć klasy walidujacej kontakt na podstawie wartości %s - błąd: ",
                validationClass, e.getMessage()), e);
    }

}

