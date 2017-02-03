package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.ContractPbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.exception.GryfValidationException;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.contracts.ContractDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.ContractEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.searchform.ContractEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.contracts.ContractValidator;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private PbeProductInstancePoolService pbeProductInstancePoolService;

    @Autowired
    private PbeProductInstancePoolLocalService pbeProductInstancePoolLocalService;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Override
    public List<GrantProgramDictionaryDTO> findGrantProgramsDictionaries() {
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
        contractValidator.validateContractSave(contract);
        return contractRepository.save(contract).getId();
    }

    private Contract fillContract(Contract entity, ContractDTO dto) {
        entity.setIndividual(dto.getIndividual() != null ? individualRepository.get(dto.getIndividual().getId()) : null);
        entity.setEnterprise(dto.getEnterprise() != null ? enterpriseRepository.get(dto.getEnterprise().getId()) : null);
        entity.setGrantProgram(dto.getGrantProgram() != null ? grantProgramRepository.get((Long)dto.getGrantProgram().getId()) : null);
        entity.setContractType((dto.getContractType() != null && dto.getContractType().getId() != null) ? contractTypeRepository.get((String)dto.getContractType().getId()) : null);
        if (dto.getTrainingCategory() != null) {
            for(String categoryId : dto.getTrainingCategory()){
                if(!StringUtils.isEmpty(categoryId)) {
                    TrainingCategory category = trainingCategoryRepository.get(categoryId);
                    if(category == null){
                        gryfValidator.validate(String.format("Nie istnieje kategoria usługi o identyfikatorze [%s]", categoryId));
                    }
                    entity.addCategory(category);
                }
            }
        }

        return entity;
    }

    @Override
    public void updateContract(ContractDTO contractDto) {
        Contract contract = contractDtoMapper.convert(contractDto);
        fillContract(contract, contractDto);
        contractValidator.validateContractUpdate(contract);
        contractRepository.update(contract, contract.getId());
    }

    @Override
    public List<DictionaryDTO> findContractTypesDictionaries() {
        List<ContractType> contractTypes = contractTypeRepository.findAll();
        return dictionaryEntityMapper.convert(contractTypes);
    }

    @Override
    public GrantProgramDictionaryDTO findGrantProgramOfFirstUserContract(String pesel) {
        Contract contract = contractRepository.findFirstContractOfUser(pesel);

        if(contract != null) {
            return grantProgramEntityMapper.convert(contract.getGrantProgram());
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Long resign(Long contractId) {
        List<ContractPbeProductInstancePoolDto> contractPools = pbeProductInstancePoolService.findPoolInstancesByContractId(contractId);
        Consumer<PbeProductInstancePoolDto> pInsPoolCons = pbeProductInstancePool -> {
            checkIfContainsAvaiablePool(pbeProductInstancePool);
            Long ermbsId = electronicReimbursementsService.createEreimbursementForReturnedPool(pbeProductInstancePool);
            pbeProductInstancePoolLocalService.returnAvaiablePools(ermbsId);
            electronicReimbursementsService.createDocuments(ermbsId);
            electronicReimbursementsService.printReports(ermbsId);
        };
        contractPools.stream().forEach(pInsPoolCons);
        return contractId;
    }

    private void checkIfContainsAvaiablePool(PbeProductInstancePoolDto pbeProductInstancePool) {
        if(pbeProductInstancePool.getAvailableNum() == null || pbeProductInstancePool.getAvailableNum() == 0){
            throw new GryfValidationException("Brak dostępnej puli bonów. Nie można utworzyć rozliczenia");
        }
    }
}
