package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ContractSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.contracts.ContractDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.ContractEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.searchform.ContractEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.contracts.ContractValidator;

import java.util.Date;
import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private GrantProgramEntityMapper grantProgramEntityMapper;

    @Autowired
    private ContractSearchDao contractSearchDao;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractDtoMapper contractDtoMapper;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private ContractEntityToSearchResultMapper contractEntityToSearchResultMapper;

    @Autowired
    private ContractEntityMapper contractEntityMapper;

    @Autowired
    private TrainingCategoryRepository trainingCategoryRepository;

    @Autowired
    private ContractValidator contractValidator;

    @Override
    public List<GrantProgramDictionaryDTO> FindGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return grantProgramEntityMapper.convert(grantPrograms);
    }
    @Override
    public ContractDTO findContract(Long id) {
        ContractDTO dto = contractEntityMapper.convert(contractRepository.get(id));
        return dto;
    }

    @Override
    public List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO contractDto) {
        List<Contract> contracts = contractRepository.findContracts(contractDto);
        return contractEntityToSearchResultMapper.convert(contracts);
    }

    @Override
    public ContractDTO createContract() {
        return null;
    }

    @Override
    public Long saveContract(ContractDTO contractDto) {
        Contract contract = contractDtoMapper.convert(contractDto);
        fillContract(contract, contractDto);
        contractValidator.validateContract(contract);
        return contractRepository.save(contract).getId();
    }

    private Contract fillContract(Contract entity, ContractDTO dto) {
        entity.setIndividual(dto.getIndividual() != null ? individualRepository.get(dto.getIndividual().getId()) : null);
        entity.setEnterprise(dto.getEnterprise() != null ? enterpriseRepository.get(dto.getEnterprise().getId()) : null);
        entity.setGrantProgram(dto.getGrantProgram() != null ? grantProgramRepository.get(dto.getGrantProgram().getGrantProgramOwnerId()) : null);
        entity.setContractType(dto.getContractType() != null ? contractTypeRepository.get((String)dto.getContractType().getId()) : null);
        for(String categoryId : dto.getTrainingCategory()){
            if(!StringUtils.isEmpty(categoryId)) {
                TrainingCategory category = trainingCategoryRepository.get(categoryId);
                entity.addCategory(category);
            }
        }

        return entity;
    }

    @Override
    public void updateContract(ContractDTO contractDto) {
        Contract contract = contractDtoMapper.convert(contractDto);
        fillContract(contract, contractDto);
        contractRepository.update(contract, contract.getId());
    }

    @Override
    public List<DictionaryDTO> findContractTypesDictionaries() {
        List<ContractType> contractTypes = contractTypeRepository.findAll();
        return dictionaryEntityMapper.convert(contractTypes);
    }
}
