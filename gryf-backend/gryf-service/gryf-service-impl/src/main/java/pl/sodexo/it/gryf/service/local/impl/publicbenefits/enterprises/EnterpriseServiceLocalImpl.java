package pl.sodexo.it.gryf.service.local.impl.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumEnterpriseExistException;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.api.other.ApplicationParametersService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidationService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.enterprises.EnterpriseServiceLocal;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class EnterpriseServiceLocalImpl implements EnterpriseServiceLocal {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ContactTypeValidationService contactTypeValidationService;

    @Autowired
    private ApplicationParametersService applicationParametersService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Override
    public Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup) {
        validateEnterprise(enterprise, checkVatRegNumDup);
        enterprise = enterpriseRepository.save(enterprise);

        enterprise.setCode(generateCode(enterprise.getId()));
        enterpriseRepository.update(enterprise, enterprise.getId());
        return enterprise;
    }

    @Override
    public Set<String> getEmailRecipients(Enterprise enterprise, Set<String> existingRecipientsSet) {
        Set<String> set;
        if (existingRecipientsSet == null) {
            set = new HashSet<>();
        } else {
            set = existingRecipientsSet;
        }
        if (enterprise != null) {
            for (EnterpriseContact contact : enterprise.getContacts()) {
                ContactType contactType = contact.getContactType();
                if (ContactType.TYPE_EMAIL.equals(contactType.getType())) {
                    if (!StringUtils.isEmpty(contact.getContactData())) {
                        set.add(contact.getContactData());
                    }
                }
            }
        }
        return set;
    }

    @Override
    public void updateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup) {
        validateEnterprise(enterprise, checkVatRegNumDup);
        enterpriseRepository.update(enterprise, enterprise.getId());
    }

    private void validateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = validateService.generateViolation(enterprise);

        //CONTACT DATA - VALIDATION
        validateContacts(enterprise.getContacts(), violations);
        validateAccountRepayment(enterprise, violations);

        //VALIDATE (EXCEPTION)
        validateService.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkVatRegNumDup) {
            List<Enterprise> enterpriseList = enterpriseRepository.findByVatRegNum(enterprise.getVatRegNum());
            validateVatRegNumExist(enterpriseList, enterprise);
        }
    }

    private void validateAccountRepayment(Enterprise enterprise, List<EntityConstraintViolation> violations) {
        boolean isEditable = securityCheckerService.hasPrivilege(Privileges.GRF_ENTERPRISE_REPAY_ACC_MOD);

        String accountRepayment = enterprise.getAccountRepayment();
        if (isEditable && StringUtils.isEmpty(accountRepayment)) {
            violations.add(new EntityConstraintViolation(Enterprise.ACCOUNT_REPAYMENT_NAME, "Konto do zwrotu środków nie może być puste", accountRepayment));
        }

        if (!isEditable) {
            if (enterprise.getId() != null) {
                Enterprise existingEnterprise = enterpriseRepository.get(enterprise.getId());
                if (!Objects.equals(existingEnterprise.getAccountRepayment(), accountRepayment)) {
                    violations.add(new EntityConstraintViolation(Enterprise.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
                }
                return;
            }
            if (!StringUtils.isEmpty(accountRepayment)) {
                violations.add(new EntityConstraintViolation(Enterprise.ACCOUNT_REPAYMENT_NAME, "Nie masz uprawnień do edycji tego pola", accountRepayment));
            }
        }
    }

    private String generateCode(Long id) {
        String prefix = applicationParametersService.getGryfEnterpriseCodePrefix();
        int zeroCount = applicationParametersService.getGryfEnterpriseCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d", prefix, id);
    }

    private void validateContacts(List<EnterpriseContact> contacts, List<EntityConstraintViolation> violations) {
        int contactsSize = contacts.size();
        EnterpriseContact[] contactTab = contacts.toArray(new EnterpriseContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeValidationService.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if (!validContractData.isValid()) {

                String path = String.format("%s[%s].%s", Enterprise.CONTACTS_ATTR_NAME, i, EnterpriseContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validateVatRegNumExist(List<Enterprise> enterprises, Enterprise enterprise) {
        enterprises.remove(enterprise);
        if (enterprises.size() > 0) {
            throw new VatRegNumEnterpriseExistException("W systemie istnieja zapisane podmioty o danym numerze NIP", EnterpriseSearchResultDTO.createList(enterprises));
        }
    }

}
