package pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidator;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.TrainingInstitutionEntityMapper;

import java.util.List;

/**
 * Walidator dla encji TrainingInstitution
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class TrainingInstitutionValidator {

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ContactTypeValidator contactTypeValidator;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private TrainingInstitutionEntityMapper trainingInstitutionEntityMapper;

    public void validateTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(trainingInstitution);

        //CONTACT DATA - VALIDATION
        validateContacts(trainingInstitution.getContacts(), violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkVatRegNumDup) {
            List<TrainingInstitution> trainingInstitutionList = trainingInstitutionRepository.findByVatRegNum(trainingInstitution.getVatRegNum());
            validateVatRegNumExist(trainingInstitutionEntityMapper.convert(trainingInstitutionList), trainingInstitution);
        }
    }

    //PROTECTED METHODS

    private void validateContacts(List<TrainingInstitutionContact> contacts, List<EntityConstraintViolation> violations) {
        int contactsSize = contacts.size();
        TrainingInstitutionContact[] contactTab = contacts.toArray(new TrainingInstitutionContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeValidator.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if (!validContractData.isValid()) {

                String path = String.format("%s[%s].%s", Enterprise.CONTACTS_ATTR_NAME, i, EnterpriseContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validateVatRegNumExist(List<TrainingInstitutionDto> trainingInstitutions, TrainingInstitution trainingInstitution) {
        trainingInstitutions.removeIf(dto -> trainingInstitution.getId().equals(dto.getId()));
        if (trainingInstitutions.size() > 0) {
            throw new VatRegNumTrainingInstitutionExistException("W systemie istnieja zapisane podmioty o danym numerze NIP", trainingInstitutions);
        }
    }
}