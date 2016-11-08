package pl.sodexo.it.gryf.service.validation.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

/**
 * Created by adziobek on 07.11.2016.
 */
@Component
public class ContractValidator {

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private AccountContractPairRepository accountContractPairRepository;

    @Autowired
    private IndividualRepository individualRepository;

    public void validateContract(Contract contract) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(contract);

        validateContractDates(contract, violations);
        validateContractId(contract, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }
    private void validateContractDates(Contract contract, List<EntityConstraintViolation> violations) {
        if (contract.getSignDate() != null && contract.getExpiryDate() != null && contract.getExpiryDate().before(contract.getSignDate())) {
            violations.add(new EntityConstraintViolation(Contract.EXPIRY_DATE_ATTR_NAME, "Data upływu ważności umowy nie mooże być wcześniejsza od daty jej podpisania", null));
        }
    }

    private void validateContractId(Contract contract, List<EntityConstraintViolation> violations) {
        if (contract.getId() != null) {
            String message = "";
            AccountContractPair accountContractPair = accountContractPairRepository.findByContractId(contract.getId());
            if (accountContractPair == null) {
                message = "Podane id umowy nie wystepuje w bazie";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }
            if (!accountContractPair.isUsed()) {
                message = "Para id umowy - subkonto istnieje ale nie została przypisana uczestnikowi";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }

            Individual individual = individualRepository.findById(contract.getIndividual().getId());
            if (individual.getAccountPayment().isEmpty()) {
                message = "Wybrany uczestnik nie ma przypisanego numeru subkonta";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }
            if (accountContractPair.getAccountPayment() != individual.getAccountPayment()) {
                message = "Numer subkonta przypisany do umowy jest inny niż posiadany przez uczestnika";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }

        } else {
            violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, "Id umowy nie może być puste", null));
        }
    }

}