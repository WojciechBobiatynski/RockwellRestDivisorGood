package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.common.exception.GryfValidationException;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.contracts.ContractDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.ContractEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.searchform.ContractEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.contracts.ContractValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by adziobek on 25.10.2016.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractServiceImpl.class);

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
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private AccountContractPairService accountContractPairService;


    @Override
    public List<GrantProgramDictionaryDTO> findGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return grantProgramEntityMapper.convert(grantPrograms);
    }
    @Override
    public ContractDTO findContract(String id) {
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
    public String saveContract(ContractDTO contractDto) {
        Contract contract = contractDtoMapper.convert(contractDto);
        fillContract(contract, contractDto);
        contractValidator.validateContractSave(contract);
        AccountContractPair accountContractPair = accountContractPairService.findByContractId(contractDto.getId());
        accountContractPairService.use(accountContractPair);
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
    public String resign(String contractId) {
        List<ContractPbeProductInstancePoolDto> contractPools = pbeProductInstancePoolService.findPoolInstancesByContractId(contractId);
        checkIfContainsAvaiablePool(contractPools);

        List<Long[]> successList = new ArrayList<>();
        Consumer<PbeProductInstancePoolDto> pInsPoolCons = pbeProductInstancePool -> {
            if(pbeProductInstancePool.getAvailableNum() > 0) {

                Long ermbsId = null;
                try {
                    ermbsId = electronicReimbursementsService.createEreimbursementForReturnedPool(pbeProductInstancePool);
                    electronicReimbursementsService.createDocuments(ermbsId);
                    electronicReimbursementsService.printReports(ermbsId);
                    successList.add(new Long[]{pbeProductInstancePool.getId(), ermbsId});

                } catch (GryfRuntimeException e){
                    serveException("biznesowy", ermbsId, pbeProductInstancePool, successList, e);
                } catch (RuntimeException e) {
                    LOGGER.error("Wysąpił bład przy rezygnacji z umowy: " + e.getMessage(), e);
                    serveException("krytyczny", ermbsId, pbeProductInstancePool, successList, e);
                }
            }
        };
        contractPools.stream().forEach(pInsPoolCons);
        return contractId;
    }

    private void checkIfContainsAvaiablePool(List<ContractPbeProductInstancePoolDto> pools) {
        boolean flag = false;
        for(ContractPbeProductInstancePoolDto p : pools){
            if(p.getAvailableNum() > 0){
                flag = true;
            }
        }
        if(!flag){
            throw new GryfValidationException("Brak dostępnych puli bonów. Nie można utworzyć rozliczeń.");
        }
    }

    private List<EntityConstraintViolation> getSuccessMessage(List<Long[]> successList){
        List<EntityConstraintViolation> result = new ArrayList<>();
        for(Long[] tab : successList){
            result.add(new EntityConstraintViolation(String.format("Dla puli '%s' poprawnie utworzono rozliczenie o identyfikatorze '%s'.",
                    tab[0], tab[1])));
        }
        return result;
    }

    private void serveException(String exceptionType, Long ermbsId, PbeProductInstancePoolDto pbeProductInstancePool,
                                List<Long[]> successList, RuntimeException e){

        List<EntityConstraintViolation> violations = getSuccessMessage(successList);
        if(ermbsId != null) {
            String statusName = electronicReimbursementsService.getReimbursmentStatusName(ermbsId);
            violations.add(new EntityConstraintViolation(String.format("Dla puli o identyfikatorze '%s' "
                            + "utworzono rozliczenie o identyfikatorze '%s' w statusie '%s'. "
                            + "Wystapił błąd %s: '%s'. "
                            + "Należy ponowić próbę zmiany statusu rozliczenia. ",
                    pbeProductInstancePool.getId(), ermbsId, statusName, exceptionType, e.getMessage())));
        }else{
            violations.add(new EntityConstraintViolation(String.format("Dla puli o identyfikatorze '%s' "
                            + "wystapił błąd %s: '%s'. ",
                    pbeProductInstancePool.getId(), exceptionType, e.getMessage())));
        }
        violations.add(new EntityConstraintViolation("Należy odświeżyć ekran umowy!!!"));
        gryfValidator.validate(violations);
    }
}
