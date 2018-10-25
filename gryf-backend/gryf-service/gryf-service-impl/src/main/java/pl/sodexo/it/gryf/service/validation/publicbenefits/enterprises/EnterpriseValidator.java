package pl.sodexo.it.gryf.service.validation.publicbenefits.enterprises;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumEnterpriseExistException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.validation.ValidationGroupEnterprise;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.searchform.EnterpriseEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.AbstractValidator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Walidator dla encji Enterprise
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class EnterpriseValidator extends AbstractValidator {

    private static final String VIOLATIONS_PREFIX = "Dla MŚP: ";
    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ContactTypeValidator contactTypeValidator;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseEntityToSearchResultMapper enterpriseEntityToSearchResultMapper;

    public void validateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup, boolean validateAccountRepayment) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(enterprise);

        //CONTACT DATA - VALIDATION
        validateContacts(enterprise.getContacts(), violations);
        if(validateAccountRepayment) {
            validateAccountRepayment(enterprise, violations);
        }

        //VALIDATE (EXCEPTION)
        addPrefixMessage(VIOLATIONS_PREFIX, violations);
        gryfValidator.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkVatRegNumDup) {
            List<Enterprise> enterpriseList = enterpriseRepository.findByVatRegNum(enterprise.getVatRegNum());
            validateVatRegNumExist(enterpriseList, enterprise);
        }
    }

    private void validateAccountRepayment(Enterprise enterprise, List<EntityConstraintViolation> violations) {
        boolean isEditable = securityChecker.hasPrivilege(Privileges.GRF_ENTERPRISE_REPAY_ACC_MOD);

        String accountRepayment = enterprise.getAccountRepayment();
        if (!isEditable) {
            if (enterprise.getId() != null) {
                Enterprise existingEnterprise = enterpriseRepository.get(enterprise.getId());
                if (!Objects.equals(existingEnterprise.getAccountRepayment(), accountRepayment)) {
                    violations.add(new EntityConstraintViolation(Enterprise.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
                }
                return;
            }
            if (!GryfStringUtils.isEmpty(accountRepayment)) {
                violations.add(new EntityConstraintViolation(Enterprise.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
            }
        }
    }

    private void validateContacts(List<EnterpriseContact> contacts, List<EntityConstraintViolation> violations) {
        int contactsSize = contacts.size();
        EnterpriseContact[] contactTab = contacts.toArray(new EnterpriseContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeValidator.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if (!validContractData.isValid()) {

                String path = String.format("%s[%s].%s", Enterprise.CONTACTS_ATTR_NAME, i, EnterpriseContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validateVatRegNumExist(List<Enterprise> enterprises, Enterprise enterprise) {
        enterprises.remove(enterprise);
        if (enterprises.size() > 0) {
            throw new VatRegNumEnterpriseExistException("W systemie istnieja zapisane podmioty o danym numerze NIP", enterpriseEntityToSearchResultMapper.convert(enterprises));
        }
    }

}
