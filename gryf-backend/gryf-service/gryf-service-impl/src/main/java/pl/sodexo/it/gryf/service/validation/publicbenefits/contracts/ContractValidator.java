package pl.sodexo.it.gryf.service.validation.publicbenefits.contracts;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportContractDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.employments.EmploymentRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.service.api.patterns.DefaultPatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.validation.publicbenefits.AbstractValidator;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adziobek on 07.11.2016.
 */
@Component
public class ContractValidator extends AbstractValidator {

    private static final String INDIVIDUAL_CONTRACT_TYPE_ID = "IND";
    private static final String ENTERPRISE_CONTRACT_TYPE_ID = "ENT";

    private static final String VIOLATIONS_PREFIX = "Dla Umowy: ";

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private AccountContractPairRepository accountContractPairRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ParamInDateService paramInDateService;


    @Autowired
    @Qualifier(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
    private PatternService grantProgramPatternService;

    public void validateContractSave(Contract contract) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(contract);

        validateContractDates(contract, violations);
        validateContractId(contract, violations);
        validateContractType(contract, violations);
        validateEnterpriseParticipant(contract, violations);

        //VALIDATE (EXCEPTION)
        addPrefixMessage(VIOLATIONS_PREFIX, violations);
        gryfValidator.validate(violations);
    }

    public void validateContractUpdate(Contract contract) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(contract);

        validateContractDates(contract, violations);
        validateContractType(contract, violations);
        validateEnterpriseParticipant(contract, violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    public List<EntityConstraintViolation> validateContractIdAgreementWithPattern(ImportContractDTO importContractDTO, String externalOrderIdPatternRegexp) {
        PatternContext importContractContext =  DefaultPatternContext.create().withCode(importContractDTO.getGrantProgram().getProgramCode())
                .withId((Long) importContractDTO.getGrantProgram().getId()).withDefaultPattern(externalOrderIdPatternRegexp).build();
        String grantProgramPatternServicePattern = grantProgramPatternService.getPattern(importContractContext);
        Pattern patternCompile = Pattern.compile(grantProgramPatternServicePattern);
        Matcher matcher = patternCompile.matcher(importContractDTO.getExternalOrderId());
        if (!matcher.matches()) {
            List<EntityConstraintViolation> contractViolations = Lists.newArrayList();
            contractViolations.add(new EntityConstraintViolation(String.format("Identyfikator umowy musi by?? w formacie kod programu/numer/numer dla danego programu")));
            addPrefixMessage(VIOLATIONS_PREFIX, contractViolations);
            return contractViolations;
        }
        return Collections.EMPTY_LIST;
    }

    private void validateContractDates(Contract contract, List<EntityConstraintViolation> violations) {
        Date currentDate = new Date();
        Date signDate  = contract.getSignDate();
        if ( signDate!= null) {
            if (signDate.after(currentDate)) {
                violations.add(new EntityConstraintViolation(Contract.SIGN_DATE_ATTR_NAME, "Data podpisania umowy nie mo??e by?? p????niejsza ni?? dzi??", null));
            }
        }
        if (signDate != null && contract.getExpiryDate() != null && contract.getExpiryDate().before(signDate)) {
            violations.add(new EntityConstraintViolation(Contract.EXPIRY_DATE_ATTR_NAME, "Data up??ywu wa??no??ci umowy nie moo??e by?? wcze??niejsza od daty jej podpisania", null));
        }
    }

    private void validateContractId(Contract contract, List<EntityConstraintViolation> violations) {
        if (contract.getId() != null && contract.getIndividual() != null) {
            String message = "";

            if (contractRepository.get(contract.getId()) != null) {
                message = "Istnieje ju?? umowa o podanym id";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }

            AccountContractPair accountContractPair = accountContractPairRepository.findByContractId(contract.getId());
            if (accountContractPair == null) {
                message = "Podane id umowy nie wystepuje w bazie";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }
            /*if (!accountContractPair.isUsed()) {
                message = "Para id umowy - subkonto istnieje ale nie zosta??a przypisana uczestnikowi";
                violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                return;
            }*/

            String accountPayment = contract.getAccountPayment();
            if (isIndividualContractType(contract) || isEnterpriseContainIndividual(contract)) {
                String individualOrEnterprise = isIndividualContractType(contract) ? "uczestnik" : (isEnterpriseContainIndividual(contract) ? "M??P" : "");
                if (contract.getAccountPayment().isEmpty()) {
                    message = String.format("Wybrany %s nie ma przypisanego numeru subkonta", individualOrEnterprise);
                    violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                    return;
                }
                if (!accountContractPair.getAccountPayment().equals(accountPayment)) {
                    message = String.format("Numer subkonta przypisany do umowy jest inny ni?? posiadany przez %s", individualOrEnterprise) ;
                    violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
                    return;
                }
            }



        }
    }

    private void validateContractType(Contract contract, List<EntityConstraintViolation> violations) {
        if(contractTypeIdNotNull(contract)){
            if (isIndividualContractType(contract)) {
                if (contract.getEnterprise() != null) {
                    violations.add(new EntityConstraintViolation(Contract.ENTERPRISE_ATTR_NAME, "Dane M??P dla rodzaju umowy 'Osoba fizyczna' powinny by?? puste", null));
                }
            }
            if(isEnterpriseContractType(contract)){
                if (contract.getEnterprise() == null) {
                    violations.add(new EntityConstraintViolation(Contract.ENTERPRISE_ATTR_NAME, "Dane M??P dla rodzaju umowy 'M??P' powinny by?? wype??nione", null));
                }
            }
        }

    }

    private boolean contractTypeIdNotNull(Contract contract) {
        if (contract.getContractType() == null || contract.getContractType().getId() == null) return false;
        return true;
    }

    private void validateEnterpriseParticipant(Contract contract, List<EntityConstraintViolation> violations) {
        if (contract.getContractType() != null && isEnterpriseContractType(contract) && contract.getEnterprise() != null) {
            if (!isEnterpriseContainIndividual(contract)) {
                violations.add(new EntityConstraintViolation(Contract.ENTERPRISE_ATTR_NAME, "Uczestnik nie nale??y do wybranego M??P", null));
            }
        }
    }

    private boolean isEnterpriseContainIndividual(Contract contract) {
        Long individualId = contract.getIndividual().getId();
        Long enterpriseId = contract.getEnterprise().getId();

        Employment employment = employmentRepository.findByIndividualIdAndEnterpriseId(individualId, enterpriseId);
        if (employment == null) {return false;}
        return true;
    }

    private boolean isEnterpriseContractType(Contract contract) {
        return contract.getContractType().getId().equals(ENTERPRISE_CONTRACT_TYPE_ID);
    }

    private boolean isIndividualContractType(Contract contract) {
        return INDIVIDUAL_CONTRACT_TYPE_ID.equals(contract.getContractType().getId());
    }

}