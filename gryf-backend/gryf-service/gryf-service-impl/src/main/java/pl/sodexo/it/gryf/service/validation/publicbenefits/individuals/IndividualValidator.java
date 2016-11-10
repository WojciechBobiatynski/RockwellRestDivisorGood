package pl.sodexo.it.gryf.service.validation.publicbenefits.individuals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;

import java.util.List;
import java.util.Objects;

/**
 * Walidator dla encji Individual
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class IndividualValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualValidator.class);

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ContactTypeValidator contactTypeValidator;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private IndividualEntityToSearchResultMapper individualEntityToSearchResultMapper;

    public void validateIndividual(Individual individual, boolean checkPeselDup) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(individual);

        //CONTACT DATA - VALIDATION
        validateContacts(individual.getContacts(), violations);
        validateAccountRepayment(individual, violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkPeselDup) {
            validatePeselExist(individual, violations);
        }

        validateRole(individual, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);

    }

    private void validateRole(Individual individual, List<EntityConstraintViolation> violations) {
        if(individual.getIndividualUser() == null){
            return;
        }
        if(individual.getIndividualUser().getRoles() == null || individual.getIndividualUser().getRoles().isEmpty()){
            violations.add(new EntityConstraintViolation("role", "Brak ról dla użytkownika"));
        }
    }

    private void validateAccountRepayment(Individual individual, List<EntityConstraintViolation> violations) {
        boolean isEditable = securityChecker.hasPrivilege(Privileges.GRF_INDIVIDUAL_REPAY_ACC_MOD);

        String accountRepayment = individual.getAccountRepayment();
        if (isEditable && GryfStringUtils.isEmpty(accountRepayment)) {
            violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME, "Konto do zwrotu środków nie może być puste", accountRepayment));
        }

        if (!isEditable) {
            if (individual.getId() != null) {
                Individual existingIndividual = individualRepository.get(individual.getId());
                if (!Objects.equals(existingIndividual.getAccountRepayment(), accountRepayment)) {
                    violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
                }
                return;
            }
            if (!GryfStringUtils.isEmpty(accountRepayment)) {
                violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
            }
        }
    }

    private void validateContacts(List<IndividualContact> contacts, List<EntityConstraintViolation> violations) {
        int contactsSize = contacts.size();
        IndividualContact[] contactTab = contacts.toArray(new IndividualContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeValidator.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if (!validContractData.isValid()) {

                String path = String.format("%s[%s].%s", Individual.CONTACTS_ATTR_NAME, i, IndividualContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validatePeselExist(Individual individual, List<EntityConstraintViolation> violations) {
        Individual ind = individualRepository.findByPesel(individual.getPesel());
        if (ind != null && !ind.getId().equals(individual.getId())) {
            violations.add(new EntityConstraintViolation(Individual.PESEL_ATTR_NAME, "W systemie jest już osoba o podanym numerze PESEL", null));
        }
    }
}

