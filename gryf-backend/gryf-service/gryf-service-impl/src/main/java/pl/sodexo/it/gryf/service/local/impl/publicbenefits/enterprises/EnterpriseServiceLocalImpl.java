package pl.sodexo.it.gryf.service.local.impl.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
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

    @Override
    public Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup) {
        enterpriseValidator.validateEnterprise(enterprise, checkVatRegNumDup);
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
        enterpriseValidator.validateEnterprise(enterprise, checkVatRegNumDup);
        enterpriseRepository.update(enterprise, enterprise.getId());
    }

    private String generateCode(Long id) {
        String prefix = applicationParameters.getGryfEnterpriseCodePrefix();
        int zeroCount = applicationParameters.getGryfEnterpriseCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d", prefix, id);
    }

}
