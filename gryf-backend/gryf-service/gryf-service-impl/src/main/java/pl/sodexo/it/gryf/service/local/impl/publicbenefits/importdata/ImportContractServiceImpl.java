package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.enums.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importContractService")
public class ImportContractServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE METHODS

    @Autowired
    private ContractService contractService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private AccountContractPairService accountContractPairService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private TrainingCategoryRepository trainingCategoryRepository;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private OrderServiceLocal orderServiceLocal;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private OrderService orderService;

    //OVERRIDE

    @Override
    protected int saveEmptyExtraRows(Long importJobId, int rowNums) {
        return 0;
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportComplexContractDTO importDTO = createComplexContractDTO(row);
        validateImport(importDTO);

        ContractType contractType = contractTypeRepository.get(importDTO.getContract().getContractType());
        List<TrainingCategory> trainingCategories = trainingCategoryRepository.findByIdList(importDTO.getContract().getContractTrainingCategoryList());

        ZipCode zipCodeIndividualInvoice = zipCodeRepository.findActiveByCode(importDTO.getIndividual().getAddressInvoice().getZipCode());
        ZipCode zipCodeEnterpriseInvoice = importDTO.checkContractType(ContractType.TYPE_ENT) ?
                                            zipCodeRepository.findActiveByCode(importDTO.getEnterprise().getAddressInvoice().getZipCode()) : null;

        validateConnectedData(importDTO, contractType, trainingCategories,
                                zipCodeIndividualInvoice, zipCodeEnterpriseInvoice);


        ImportResultDTO importResult = saveContractData(importDTO, paramsDTO,
                                                        contractType, trainingCategories,
                                                        zipCodeIndividualInvoice, zipCodeEnterpriseInvoice);
        importResult.setDescrption(String.format("Poprawno utworzono dane: umowa (%s) użytkownik (%s), MŚP (%s), zamówienie (%s)",
                                    getIdToDescription(importResult.getContractId()),
                                    getIdToDescription(importResult.getIndividualId()),
                                    getIdToDescription(importResult.getEnterpriseId()),
                                    getIdToDescription(importResult.getOrderId())));
        return importResult;
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportComplexContractDTO importDTO){
        List<EntityConstraintViolation> contractViolations = gryfValidator.generateViolation(importDTO.getContract());
        addPrefixMessage("Dla umowy: ", contractViolations);

        List<EntityConstraintViolation> individualViolations = gryfValidator.generateViolation(importDTO.getIndividual());
        addPrefixMessage("Dla uczestnika: ", individualViolations);

        List<EntityConstraintViolation> enterpriseViolations = Lists.newArrayList();
        if(importDTO.checkContractType(ContractType.TYPE_ENT)){
            enterpriseViolations.addAll(gryfValidator.generateViolation(importDTO.getEnterprise()));
        }else if(importDTO.checkContractType(ContractType.TYPE_IND)){
            if(!importDTO.getEnterprise().isEmpty()){
                enterpriseViolations.add(new EntityConstraintViolation("Dla typu kontraktu 'IND' (osoba fizyczna) pola dla MŚP powinny być puste."));
            }
        }
        addPrefixMessage("Dla MŚP: ", enterpriseViolations);

        List<EntityConstraintViolation> allViolation = Lists.newArrayList();
        allViolation.addAll(contractViolations);
        allViolation.addAll(individualViolations);
        allViolation.addAll(enterpriseViolations);
        gryfValidator.validate(allViolation);
    }

    private void validateConnectedData(ImportComplexContractDTO importDTO, ContractType contractType, List<TrainingCategory> trainingCategories,
                                        ZipCode zipCodeIndividualInvoice, ZipCode zipCodeEnterpriseInvoice){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(contractType == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono rodzaju umowy "
                    + "o identyfikatorze (%s)", importDTO.getContract().getContractType())));
        }

        Map<String, TrainingCategory> trainingCategoryMap = GryfUtils.constructMap(trainingCategories, new GryfUtils.MapConstructor<String, TrainingCategory>() {
            public boolean isAddToMap(TrainingCategory input) {
                return true;
            }
            public String getKey(TrainingCategory input) {
                return input.getId();
            }
        });

        for(String c : importDTO.getContract().getContractTrainingCategoryList()){
            if(!trainingCategoryMap.containsKey(c)){
                violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kategori szkolenia przydzielonej użytkownikowi "
                        + "o identyfikatorze (%s)", c)));
            }
        }

        if(zipCodeIndividualInvoice == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla adresu użytkownika do faktury "
                    + "o wartości (%s)", importDTO.getIndividual().getAddressInvoice().getZipCode())));
        }
        if(importDTO.checkContractType(ContractType.TYPE_ENT)){
            if(zipCodeEnterpriseInvoice == null){
                violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla MŚP do faktury "
                        + "o wartości (%s)", importDTO.getEnterprise().getAddressInvoice().getZipCode())));
            }
        }

        gryfValidator.validate(violations);
    }


    private ImportResultDTO saveContractData(ImportComplexContractDTO importDTO, ImportParamsDTO paramsDTO,
                                            ContractType contractType, List<TrainingCategory> trainingCategories,
                                            ZipCode zipCodeIndividualInvoice, ZipCode zipCodeEnterpriseInvoice){

        Long contractId = importDTO.getContract().getId();
        Long enterpriseId = null;
        Long individualId = null;
        Long orderId = null;

        if(ContractType.TYPE_ENT.equals(importDTO.getContract().getContractType())){
            EnterpriseDto enterpriseDTO = createEnterpriseDTO(importDTO.getEnterprise(), zipCodeEnterpriseInvoice);
            enterpriseId = enterpriseService.saveEnterpriseDto(enterpriseDTO, false, false);
        }


        IndividualDto individualDTO = createIndividualDTO(importDTO.getIndividual(),
                                                            zipCodeIndividualInvoice,
                                                            contractId, enterpriseId);
        individualId = individualService.saveIndividual(individualDTO, true, false);
        individualRepository.get(individualId);

        ContractDTO contractDTO = createContractDTO(importDTO.getContract(), paramsDTO,
                                                contractType, trainingCategories,
                                                individualId, enterpriseId);
        contractService.saveContract(contractDTO);

        Contract contract = contractRepository.get(contractId);
        CreateOrderDTO orderDTO = createCreateOrderDTO(importDTO.getContract(), contract);
        orderId = orderService.createOrder(orderDTO);

        ImportResultDTO result = new ImportResultDTO();
        result.setContractId(contractId);
        result.setEnterpriseId(enterpriseId);
        result.setIndividualId(individualId);
        result.setOrderId(orderId);

        return result;
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportComplexContractDTO createComplexContractDTO(Row row){
        ImportComplexContractDTO c = new ImportComplexContractDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    c.getContract().setExternalOrderId(getStringCellValue(cell));
                    break;
                case 1:
                    c.getContract().setContractType(getStringCellValue(cell));
                    break;
                case 2:
                    c.getContract().setSignDate(getDateCellValue(cell));
                    break;
                case 3:
                    c.getContract().setProductInstanceNum(getIntegerCellValue(cell));
                    break;
                case 4:
                    c.getContract().setExpiryDate(getDateCellValue(cell));
                    break;
                case 5:
                    c.getContract().setContractTrainingCategories(getStringCellValue(cell));
                    break;
                case 6:
                    c.getIndividual().setFirstName(getStringCellValue(cell));
                    break;
                case 7:
                    c.getIndividual().setLastName(getStringCellValue(cell));
                    break;
                case 8:
                    c.getIndividual().setPesel(getStringCellValue(cell));
                    break;
                case 9:
                    c.getIndividual().getAddressInvoice().setStreet(getStringCellValue(cell));
                    break;
                case 10:
                    c.getIndividual().getAddressInvoice().setHomeNumber(getStringCellValue(cell));
                    break;
                case 11:
                    c.getIndividual().getAddressInvoice().setFlatNumber(getStringCellValue(cell));
                    break;
                case 12:
                    c.getIndividual().getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 13:
                    c.getIndividual().getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 14:
                    c.getIndividual().setEmail(getStringCellValue(cell));
                    break;
                case 15:
                    c.getEnterprise().setName(getStringCellValue(cell));
                    break;
                case 16:
                    c.getEnterprise().setVatRegNum(getStringCellValue(cell));
                    break;
                case 17:
                    c.getEnterprise().getAddressInvoice().setStreet(getStringCellValue(cell));
                    break;
                case 18:
                    c.getEnterprise().getAddressInvoice().setHomeNumber(getStringCellValue(cell));
                    break;
                case 19:
                    c.getEnterprise().getAddressInvoice().setFlatNumber(getStringCellValue(cell));
                    break;
                case 20:
                    c.getEnterprise().getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 21:
                    c.getEnterprise().getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 22:
                    c.getEnterprise().setEmail(getStringCellValue(cell));
                    break;
            }
        }
        return c;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private EnterpriseDto createEnterpriseDTO(ImportEnterpriseDTO importDTO, ZipCode zipCodeEnterpriseInvoice){
        EnterpriseDto dto = new EnterpriseDto();

        dto.setCode(null);
        dto.setAccountPayment(null);
        dto.setAccountRepayment(null);
        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(!importDTO.getAddressInvoice().isEmpty()) {
            ImportAddressInvoiceDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(zipCodeEnterpriseInvoice));
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(zipCodeEnterpriseInvoice));
        }

        EnterpriseContactDto contactDTO = new EnterpriseContactDto();
        contactDTO.setContactType(new ContactTypeDto());
        contactDTO.getContactType().setType(ContactType.TYPE_EMAIL);
        contactDTO.setContactData(importDTO.getEmail());
        dto.setContacts(Lists.newArrayList(contactDTO));

        return dto;
    }

    private IndividualDto createIndividualDTO(ImportIndividualDTO importDTO,
                                            ZipCode zipCodeIndividualInvoice,
                                            Long contractId, Long enterpriseId){
        IndividualDto dto = new IndividualDto();

        AccountContractPair pair = accountContractPairService.getValidAccountContractPairForUsed(contractId);
        dto.setCode(accountContractPairService.getCodeFromAccount(pair));
        dto.setAccountPayment(pair.getAccountPayment());

        dto.setFirstName(importDTO.getFirstName());
        dto.setLastName(importDTO.getLastName());
        dto.setPesel(importDTO.getPesel());

        dto.setSex(createSex(importDTO));
        dto.setDocumentType(null);
        dto.setDocumentNumber(null);
        if(!importDTO.getAddressInvoice().isEmpty()) {
            ImportAddressInvoiceDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(zipCodeIndividualInvoice));
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(zipCodeIndividualInvoice));
        }
        dto.setRemarks(null);
        dto.setVerificationCode(null);
        dto.setLastLoginDate(null);

        IndividualContactDto contactDTO = new IndividualContactDto();
        contactDTO.setContactType(new ContactTypeDto());
        contactDTO.getContactType().setType(ContactType.TYPE_VER_EMAIL);
        contactDTO.setContactData(importDTO.getEmail());
        dto.setContacts(Lists.newArrayList(contactDTO));

        if(enterpriseId != null){
            EnterpriseDto enterpriseDTO = new EnterpriseDto();
            enterpriseDTO.setId(enterpriseId);
            dto.setEnterprises(Lists.newArrayList(enterpriseDTO));
        }else{
            dto.setEnterprises(Lists.newArrayList());
        }

        RoleDto roleDTO = new RoleDto();
        roleDTO.setCode(Privileges.IND_DESKTOP_ROLE.name());
        dto.setRoles(Lists.newArrayList(roleDTO));

        return dto;
    }

    private ContractDTO createContractDTO(ImportContractDTO importDTO, ImportParamsDTO paramsDTO,
                                            ContractType contractType, List<TrainingCategory> trainingCategories,
                                            Long individualId, Long enterpriseId){
        ContractDTO dto = new ContractDTO();
        dto.setId(importDTO.getId());
        dto.setSignDate(importDTO.getSignDate());
        dto.setExpiryDate(importDTO.getSignDate());
        dto.setTrainingCategory(importDTO.getContractTrainingCategoryList());
        dto.setContractType(new DictionaryDTO(importDTO.getContractType()));

        GrantProgramDictionaryDTO grantProgram = new GrantProgramDictionaryDTO();
        grantProgram.setId(paramsDTO.getGrantProgramId());
        dto.setGrantProgram(grantProgram);

        IndividualSearchResultDTO individual = new IndividualSearchResultDTO();
        individual.setId(individualId);
        dto.setIndividual(individual);

        if(enterpriseId != null) {
            EnterpriseSearchResultDTO enterprise = new EnterpriseSearchResultDTO();
            enterprise.setId(enterpriseId);
            dto.setEnterprise(enterprise);
        }

        return dto;
    }

    private CreateOrderDTO createCreateOrderDTO(ImportContractDTO importDTO, Contract contract){
        CreateOrderDTO dto = orderServiceLocal.createCreateOrderDTO(contract);
        dto.setExternalOrderId(importDTO.getExternalOrderId());
        dto.setOrderDate(importDTO.getSignDate());
        dto.setProductInstanceNum(importDTO.getProductInstanceNum());
        return dto;
    }

    private String createSex(ImportIndividualDTO importDTO){
        Boolean isMale = PeselUtils.isMale(importDTO.getPesel());
        if(isMale == null){
            return null;
        }
        return isMale ? Sex.M.name() : Sex.K.name();
    }

    private void addPrefixMessage(String prefix, List<EntityConstraintViolation> violations){
        for(EntityConstraintViolation v : violations){
            addPrefixMessage(prefix, v);
        }
    }

    private void addPrefixMessage(String prefix, EntityConstraintViolation violation){
        violation.setMessage(prefix + violation.getMessage());
    }

}
