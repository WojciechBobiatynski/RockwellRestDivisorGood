package pl.sodexo.it.gryf.service.impl.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.individuals.IndividualDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals.IndividualEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;
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

        individual.setCode(generateCode(individual.getId()));
        individualRepository.update(individual, individual.getId());
        return individual.getId();
    }
    
    @Override
    public Set<String> getEmailRecipients(IndividualDto individualDto, Set<String> existingRecipientsSet) {
        Set<String> set;
        if (existingRecipientsSet == null){
            set = new HashSet<>();
        }
        else {
            set = existingRecipientsSet;
        }
        if (individualDto != null) {
            for (IndividualContactDto contact : individualDto.getContacts()) {
                ContactTypeDto contactType = contact.getContactType();
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
