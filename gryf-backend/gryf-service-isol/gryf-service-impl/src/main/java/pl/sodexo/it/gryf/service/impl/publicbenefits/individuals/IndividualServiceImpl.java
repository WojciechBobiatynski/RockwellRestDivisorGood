package pl.sodexo.it.gryf.service.impl.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.PeselIndividualExistException;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.service.api.ApplicationParametersService;
import pl.sodexo.it.gryf.service.api.SecurityCheckerService;
import pl.sodexo.it.gryf.service.api.dictionaries.ContactTypeService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class IndividualServiceImpl implements IndividualService {

    //FIELDS

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ContactTypeService contactTypeService;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private ApplicationParametersService applicationParametersService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    //PUBLIC METHODS

    @Override
    public Individual findIndividual(Long id) {
        return individualRepository.getForUpdate(id);
    }

    @Override
    public List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO individual) {
        List<Individual> individuals = individualRepository.findIndividuals(individual);
        return IndividualSearchResultDTO.createList(individuals);
    }

    @Override
    public Individual createIndividual() {
        Individual individual = new Individual();
        return individual;
    }

    @Override
    public Individual saveIndividual(Individual individual, boolean checkPeselDup) {
        validateIndividual(individual, checkPeselDup);
        individual = individualRepository.save(individual);

        individual.setCode(generateCode(individual.getId()));
        individualRepository.update(individual, individual.getId());
        return individual;
    }
    
    @Override
    public Set<String> getEmailRecipients(Individual individual, Set<String> existingRecipientsSet){
        Set<String> set;
        if (existingRecipientsSet == null){
            set = new HashSet<>();
        }
        else {
            set = existingRecipientsSet;
        }
        if (individual != null) {
            for (IndividualContact contact : individual.getContacts()) {
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
    public void updateIndividual(Individual individual, boolean checkPeselDup) {
        validateIndividual(individual, checkPeselDup);
        individualRepository.update(individual, individual.getId());
    }

    //PRIVATE METHODS

    private void validateIndividual(Individual individual, boolean checkPeselDup) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = validateService.generateViolation(individual);

        //CONTACT DATA - VALIDATION
        validateContacts(individual.getContacts(), violations);
        validateAccountRepayment(individual, violations);

        //VALIDATE (EXCEPTION)
        validateService.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if (checkPeselDup) {
            List<Individual> individualList = individualRepository.findByPesel(individual.getPesel());
            validatePeselExist(individualList, individual);
        }
    }

    private void validateAccountRepayment(Individual individual, List<EntityConstraintViolation>
            violations) {
        boolean isEditable = securityCheckerService.hasPrivilege(Privileges.GRF_INDIVIDUAL_REPAY_ACC_MOD);

        String accountRepayment = individual.getAccountRepayment();
        if (isEditable && StringUtils.isEmpty(accountRepayment)) {
            violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME,
                    "Konto do zwrotu środków nie może być puste", accountRepayment));
        }

        if (!isEditable) {
            if (individual.getId() != null) {
                Individual existingIndividual = individualRepository.get(individual.getId());
                if (!Objects.equals(existingIndividual.getAccountRepayment(), accountRepayment)) {
                    violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME,
                            "Nie masz uprawnień do edycji tego pola", accountRepayment));
                }
                return;
            }
            if (!StringUtils.isEmpty(accountRepayment)) {
                violations.add(new EntityConstraintViolation(Individual.ACCOUNT_REPAYMENT_NAME,
                        "Nie masz uprawnień do edycji tego pola", accountRepayment));
            }
        }
    }

    private String generateCode(Long id) {
        String prefix = applicationParametersService.getGryfIndividualCodePrefix();
        int zeroCount = applicationParametersService.getGryfIndividualCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d", prefix, id);
    }

    private void validateContacts(List<IndividualContact> contacts, List<EntityConstraintViolation> violations){
        int contactsSize = contacts.size();
        IndividualContact[] contactTab = contacts.toArray(new IndividualContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeService.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if(!validContractData.isValid()){

                String path = String.format("%s[%s].%s", Individual.CONTACTS_ATTR_NAME, i, IndividualContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validatePeselExist(List<Individual> individuals, Individual individual){
        individuals.remove(individual);
        if(individuals.size() > 0){
            throw new PeselIndividualExistException("W systemie istnieja zapisane podmioty o danym numerze PESEL", IndividualSearchResultDTO.createList(individuals));
        }
    }

}
