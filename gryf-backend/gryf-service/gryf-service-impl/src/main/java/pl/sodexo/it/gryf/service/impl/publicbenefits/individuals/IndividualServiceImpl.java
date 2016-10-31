package pl.sodexo.it.gryf.service.impl.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.individuals.IndividualDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.IndividualEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.individuals.IndividualValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class IndividualServiceImpl implements IndividualService {

    //FIELDS

    @Autowired
    private IndividualRepository individualRepository;

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
    private VerificationService verificationService;

    @Autowired
    private IndividualUserService individualUserService;

    //PUBLIC METHODS

    @Override
    public IndividualDto findIndividual(Long id) {
        return individualEntityMapper.convert(individualRepository.getForUpdate(id));
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
        individualValidator.validateIndividual(individual, checkPeselDup);
        individual = individualRepository.save(individual);

        String newVerificationCode = verificationService.createVerificationCode();

        IndividualUser user = new IndividualUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setVerificationCode(passwordEncoder.encode(newVerificationCode));
        user.setActive(true);
        user.setIndividual(individual);
        individual.setIndividualUser(user);

        //TODO: zgodnie z analizą będziemy otrzymywać numer subkonta, tutaj będzie trzeba to zmieniać. Jest w bazie trigger TRG_BU_IND_ACCOUNT, trzeba będzie go wyłączyć
        individual.setCode(generateCode(individual.getId()));
        individualRepository.save(individual);

        String verEmail = null;
        for (IndividualContact ind : individual.getContacts()) {
            if (applicationParameters.getVerEmailContactType().equals(ind.getContactType().getType())) {
                verEmail = ind.getContactData();
            }
        }

        mailService.scheduleMail(mailDtoCreator.createMailDTOForVerificationCode(verEmail, newVerificationCode));

        return individual.getId();
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

}
