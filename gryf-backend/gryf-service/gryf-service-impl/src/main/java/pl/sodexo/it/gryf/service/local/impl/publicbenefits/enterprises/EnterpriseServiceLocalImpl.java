package pl.sodexo.it.gryf.service.local.impl.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.annotation.technical.asynch.ReplacedBy;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.enterprises.EnterpriseServiceLocal;
import pl.sodexo.it.gryf.service.validation.publicbenefits.enterprises.EnterpriseValidator;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class EnterpriseServiceLocalImpl implements EnterpriseServiceLocal {

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseValidator enterpriseValidator;

    @Autowired
    private AccountContractPairService accountContractPairService;

    @Override
    public Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup, boolean validateAccountRepayment) {
        enterpriseValidator.validateEnterprise(enterprise, checkVatRegNumDup, validateAccountRepayment);
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
                    if (!GryfStringUtils.isEmpty(contact.getContactData())) {
                        set.add(contact.getContactData());
                    }
                }
            }
        }
        return set;
    }

    @Override
    public void updateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup, boolean validateAccountRepayment) {
        enterpriseValidator.validateEnterprise(enterprise, checkVatRegNumDup, validateAccountRepayment);
        enterpriseRepository.update(enterprise, enterprise.getId());
    }

}
