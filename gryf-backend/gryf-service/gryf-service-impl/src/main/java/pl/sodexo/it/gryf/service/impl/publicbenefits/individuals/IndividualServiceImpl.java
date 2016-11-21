package pl.sodexo.it.gryf.service.impl.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.IndDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.individuals.IndividualDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.RoleDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.IndividualEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.individuals.IndividualValidator;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class IndividualServiceImpl implements IndividualService {

    private static final int INVALID_ACCOUNT_FORMAT_ERROR_CODE = 20002;

    //FIELDS

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private IndividualSearchDao individualSearchDao;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private IndividualEntityMapper individualEntityMapper;

    @Autowired
    private IndividualDtoMapper individualDtoMapper;

    @Autowired
    private IndividualEntityToSearchResultMapper individualEntityToSearchResultMapper;

    @Autowired
    private IndividualValidator individualValidator;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailDtoCreator mailDtoCreator;

    @Autowired
    GryfAccessCodeGenerator gryfAccessCodeGenerator;

    @Autowired
    private AccountContractPairRepository accountContractPairRepository;

    @Autowired
    private RoleDtoMapper roleDtoMapper;

    //PUBLIC METHODS

    @Override
    public IndividualDto findIndividual(Long id) {
        return individualEntityMapper.convert(individualRepository.getForUpdate(id));
    }

    @Override
    public IndDto findIndividualAfterLogin() {
        return individualSearchDao.findIndividualAfterLogin();
    }

    @Override
    public IndividualDto findIndividualByPesel(String pesel) {
        return individualEntityMapper.convert(individualRepository.findByPesel(pesel));
    }

    @Override
    public List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO individual) {
        List<Individual> individuals = individualRepository.findIndividuals(individual);
        return individualEntityToSearchResultMapper.convert(individuals);
    }

    @Override
    public IndividualDto createIndividual() {
        return new IndividualDto();
    }

    @Override
    public Long saveIndividual(IndividualDto individualDto, boolean checkPeselDup) {
        Individual individual = individualDtoMapper.convert(individualDto);
        IndividualUser user = createIndividualUser(individual, individualDto);
        individual.setIndividualUser(user);
        individualValidator.validateIndividual(individual, checkPeselDup);
        individual = createIndividualByCode(individual);
        individualRepository.save(individual);

        return individual.getId();
    }

    private IndividualUser createIndividualUser(Individual individual, IndividualDto individualDto) {
        String newVerificationCode = gryfAccessCodeGenerator.createVerificationCode();

        IndividualUser user = new IndividualUser();
        user.setVerificationCode(AEScryptographer.encrypt(newVerificationCode));
        user.setActive(true);
        user.setIndividual(individual);
        user.setRoles(roleDtoMapper.convert(individualDto.getRoles()));
        return user;
    }

    private Individual createIndividualByCode(Individual individual) {
        if (individual.getCode() == null) {
            individual = individualRepository.save(individual);
            individual.setCode(generateCode(individual.getId()));
        } else {
            String account = getAccount(individual);
            AccountContractPair accountContractPair = getAccountContractPair(account);
            individual.setId(getIdFromGeneratedCode(individual.getCode()));
            individual.setAccountPayment(account);
            accountContractPair.setUsed(true);
            accountContractPairRepository.save(accountContractPair);
        }
        return individual;
    }

    @Override
    public void sendEmailNotification(IndividualDto individualDto, String appUrl) {
        mailService.scheduleMail(mailDtoCreator.createMailDTOForVerificationCode(individualDto, appUrl));
    }

    private AccountContractPair getAccountContractPair(String account) {
        AccountContractPair accountContractPair = accountContractPairRepository.findByAccountPayment(account);
        if (accountContractPair == null) {
            EntityConstraintViolation entityConstraintViolation = new EntityConstraintViolation(Individual.CODE_ATTR_NAME, "Niepoprawny kod osoby fizycznej");
            throw new EntityValidationException(Arrays.asList(entityConstraintViolation));
        }
        if (accountContractPair.isUsed()) {
            EntityConstraintViolation entityConstraintViolation = new EntityConstraintViolation(Individual.CODE_ATTR_NAME, "Wpisany kod jest już zarezerwowany");
            throw new EntityValidationException(Arrays.asList(entityConstraintViolation));
        }
        return accountContractPair;
    }

    private String getAccount(Individual individual) {
        String account = null;
        try {
            account = accountContractPairRepository.findAccountByCode(individual.getCode());
        } catch (PersistenceException e) {
            Throwable exc = e.getCause().getCause();
            if (exc instanceof SQLException) {
                if (((SQLException) exc).getErrorCode() == INVALID_ACCOUNT_FORMAT_ERROR_CODE) {
                    EntityConstraintViolation entityConstraintViolation = new EntityConstraintViolation(Individual.CODE_ATTR_NAME, "Niepoprawny format kodu osoby fizycznej");
                    throw new EntityValidationException(Arrays.asList(entityConstraintViolation));
                }
            }
            ;
        }
        return account;
    }

    @Override
    public Set<String> getEmailRecipients(IndividualDto individualDto, Set<String> existingRecipientsSet) {
        Set<String> set;
        if (existingRecipientsSet == null) {
            set = new HashSet<>();
        } else {
            set = existingRecipientsSet;
        }
        if (individualDto != null) {
            for (IndividualContactDto contact : individualDto.getContacts()) {
                ContactTypeDto contactType = contact.getContactType();
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
    public void updateIndividual(IndividualDto individualDto, boolean checkPeselDup) {
        Individual individual = individualDtoMapper.convert(individualDto);
        individualValidator.validateIndividual(individual, checkPeselDup);
        individualRepository.update(individual, individual.getId());
    }

    //PRIVATE METHODS

    private String generateCode(Long id) {
        String prefix = applicationParameters.getGryfIndividualCodePrefix();
        int zeroCount = applicationParameters.getGryfIndividualCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d", prefix, id);
    }

    private Long getIdFromGeneratedCode(String code) {
        return Long.parseLong(code.substring(applicationParameters.getGryfIndividualCodePrefix().length()));
    }

    @Override
    public UserTrainingReservationDataDto findUserTrainingReservationData(String pesel) {
        return individualSearchDao.findDataForTrainingReservation(pesel);
    }
}
