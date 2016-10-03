package pl.sodexo.it.gryf.service.validation.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.PeselIndividualExistException;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidator;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;

import java.util.List;
import java.util.Objects;

/**
 * Walidator dla encji Individual
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class IndividualValidator {

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

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkPeselDup) {
            List<Individual> individualList = individualRepository.findByPesel(individual.getPesel());
            validatePeselExist(individualList, individual);
        }
    }

    private void validateAccountRepayment(Individual individual, List<EntityConstraintViolation> violations) {
        boolean isEditable = securityChecker.hasPrivilege(Privileges.GRF_INDIVIDUAL_REPAY_ACC_MOD);

        String accountRepayment = individual.getAccountRepayment();
        if (isEditable && StringUtils.isEmpty(accountRepayment)) {
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
            if (!StringUtils.isEmpty(accountRepayment)) {
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

    private void validatePeselExist(List<Individual> individuals, Individual individual) {
        individuals.remove(individual);
        if (individuals.size() > 0) {
            throw new PeselIndividualExistException("W systemie istnieja zapisane podmioty o danym numerze PESEL", individualEntityToSearchResultMapper.convert(individuals));
        }
    }
}
