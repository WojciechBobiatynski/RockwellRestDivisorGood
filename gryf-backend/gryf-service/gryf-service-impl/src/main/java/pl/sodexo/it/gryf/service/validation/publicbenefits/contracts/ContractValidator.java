package pl.sodexo.it.gryf.service.validation.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

/**
 * Created by adziobek on 07.11.2016.
 */
@Component
public class ContractValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateContract(Contract contract) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(contract);

        validateContractDates(contract, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    private void validateContractDates(Contract contract, List<EntityConstraintViolation> violations) {
        if (contract.getSignDate() != null && contract.getExpiryDate() != null && contract.getExpiryDate().before(contract.getSignDate())) {
            violations.add(new EntityConstraintViolation(Contract.EXPIRY_DATE_ATTR_NAME, "Data upływu ważności umowy nie mooże być wcześniejsza od daty jej podpisania", null));
        }
    }

}