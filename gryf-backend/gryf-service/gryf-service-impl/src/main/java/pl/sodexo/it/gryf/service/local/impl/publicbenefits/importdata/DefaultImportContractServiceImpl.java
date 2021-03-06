package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramParamRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.enums.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.generator.IdentityGeneratorService;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.validation.publicbenefits.contracts.ContractValidator;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Isolution on 2016-12-02.
 */
@Transactional
public abstract class DefaultImportContractServiceImpl extends ImportBaseDataServiceImpl {

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

    @Autowired
    private ContractValidator contractValidator;

    @Autowired
    @Qualifier(IdentityGeneratorService.CONTRACT_IDENTITY_GENERATOR_CONTRACT_ID)
    private IdentityGeneratorService identityGeneratorService;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private GrantProgramParamRepository grantProgramParamRepository;

    @Value("${gryf2.service.import.checkPeselDuplication:false}")
    private Boolean checkPeselDuplication;

    @Value("${gryf2.service.import.checkValRegDuplication:false}")
    private Boolean checkValRegDuplication;

    public abstract String getExternaOrderIdPattern ();

    //OVERRIDE
    @Override
    protected int saveEmptyExtraRows(Long importJobId, int rowNums) {
        return 0;
    }

    @Override
    protected ImportResultDTO saveInternalImportDataRowBeforeSaveData(ImportParamsDTO paramsDTO, Row row){
        return new ImportResultDTO();
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row, Long importJobId) {
        ImportComplexContractDTO importComplexContractDTO = createComplexContractDTO(paramsDTO, row);

        //Jezeli MSP (ENT) i brak adresu dla IND przepisz z ENT
        copyAddressDataEnterpriseToIndividual(importComplexContractDTO);

        setDefaultOwnContributionPercentage(importComplexContractDTO, paramsDTO);

        validateImport(importComplexContractDTO);

        ContractType contractType = contractTypeRepository.get(importComplexContractDTO.getContract().getContractType());
        List<TrainingCategory> trainingCategories = trainingCategoryRepository.findByIdList(importComplexContractDTO.getContract().getContractTrainingCategories());

        AccountContractPair pair = accountContractPairService.getValidAccountContractPairForUsedByContractId(importComplexContractDTO.getContract().getId(identityGeneratorService.getGenerator(importComplexContractDTO.getContract())));

        ZipCode zipCodeIndividualInvoice = zipCodeRepository.findActiveByCode(importComplexContractDTO.getIndividual().getAddressInvoice().getZipCode());
        ZipCode zipCodeEnterpriseInvoice = importComplexContractDTO.checkContractType(ContractType.TYPE_ENT) ?
                                            zipCodeRepository.findActiveByCode(importComplexContractDTO.getEnterprise().getAddressInvoice().getZipCode()) : null;

        validateConnectedData(importComplexContractDTO, contractType, trainingCategories, pair,
                                zipCodeIndividualInvoice, zipCodeEnterpriseInvoice);

        ImportResultDTO importResult = saveContractData(importComplexContractDTO, paramsDTO,
                                                        contractType, trainingCategories, pair,
                                                        zipCodeIndividualInvoice, zipCodeEnterpriseInvoice);

        String additionalDescription = getAdditionalDescription(importComplexContractDTO, paramsDTO);

        importResult.setDescrption(String.format("%sPoprawnie utworzono dane: umowa (%s) u??ytkownik (%s), M??P (%s), zam??wienie (%s).",
                additionalDescription,
                getIdToDescription(importResult.getContractId()),
                getIdToDescription(importResult.getIndividualId()),
                getIdToDescription(importResult.getEnterpriseId()),
                getIdToDescription(importResult.getOrderId())));

        return importResult;
    }

    private void copyAddressDataEnterpriseToIndividual(ImportComplexContractDTO importComplexContractDTO) {
        if (Objects.nonNull(importComplexContractDTO)) {
            ImportEnterpriseDTO importEnterpriseDTO = importComplexContractDTO.getEnterprise();
            ImportIndividualDTO importIndividualDTO = importComplexContractDTO.getIndividual();
            if (ContractType.TYPE_ENT.equals(importComplexContractDTO.getContract().getContractType()) && Objects.isNull(importIndividualDTO.getAddressInvoice())) {
                importIndividualDTO.setAddressInvoice(importEnterpriseDTO.getAddressInvoice());
            }
        }
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
                enterpriseViolations.add(new EntityConstraintViolation("Dla typu kontraktu 'IND' (osoba fizyczna) pola dla M??P powinny by?? puste."));
            }
        }

        addPrefixMessage("Dla M??P: ", enterpriseViolations);



        List<EntityConstraintViolation> allViolation = Lists.newArrayList();

        allViolation.addAll(contractViolations);
        //walidacja identyfikatora kontraktu
        allViolation.addAll(contractValidator.validateContractIdAgreementWithPattern(importDTO.getContract(), this.getExternaOrderIdPattern()));
        allViolation.addAll(individualViolations);
        allViolation.addAll(enterpriseViolations);

        gryfValidator.validate(allViolation);
    }

    private void validateConnectedData(ImportComplexContractDTO importDTO, ContractType contractType, List<TrainingCategory> trainingCategories,
                                        AccountContractPair pair, ZipCode zipCodeIndividualInvoice, ZipCode zipCodeEnterpriseInvoice){
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

        for(String c : importDTO.getContract().getContractTrainingCategories()){
            if(!trainingCategoryMap.containsKey(c)){
                violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kategori us??ugi przydzielonej u??ytkownikowi "
                        + "o identyfikatorze (%s)", c)));
            }
        }

        if(zipCodeIndividualInvoice == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla adresu u??ytkownika do faktury "
                    + "o warto??ci (%s)", importDTO.getIndividual().getAddressInvoice().getZipCode())));
        }
        if(importDTO.checkContractType(ContractType.TYPE_ENT)){
            if(zipCodeEnterpriseInvoice == null){
                violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla M??P do faktury "
                        + "o warto??ci (%s)", importDTO.getEnterprise().getAddressInvoice().getZipCode())));
            }
        }
        if(pair == null){
            violations.add(new EntityConstraintViolation(String.format("Dla danej umowy nie znaleziono rekordu pary "
                    + "(identyfikator umowy, numer konta)")));
        }

        gryfValidator.validate(violations);
    }


    private ImportResultDTO saveContractData(ImportComplexContractDTO importComplexContractDTO, ImportParamsDTO paramsDTO,
                                            ContractType contractType, List<TrainingCategory> trainingCategories, AccountContractPair individualOrEnterpriseAccountContractPair,
                                            ZipCode zipCodeIndividualInvoice, ZipCode zipCodeEnterpriseInvoice){

        String contractId = importComplexContractDTO.getContract().getId(identityGeneratorService.getGenerator(importComplexContractDTO.getContract()));
        Long enterpriseId = null;
        Long individualId = null;
        Long orderId = null;

        //Check if Account is used
        validateUsageContractID(contractId, individualOrEnterpriseAccountContractPair);


        //CREATE ENTERPRISE
        String importContractType = importComplexContractDTO.getContract().getContractType();
        if(ContractType.TYPE_ENT.equals(importContractType)){
            EnterpriseDto enterpriseDTO = createEnterpriseDTO(importComplexContractDTO.getEnterprise(), zipCodeEnterpriseInvoice);
            EnterpriseDto enterpriseDto = enterpriseService.validateAndSaveOrUpdate(enterpriseDTO, checkValRegDuplication, false);
            enterpriseId = enterpriseDto.getId();
        }


        //CREATE INDIVIDUAL
        IndividualDto individualDTO = createIndividualDTO(importComplexContractDTO.getIndividual(),
                                        zipCodeIndividualInvoice, enterpriseId);

        individualId = individualService.validateAndSaveOrUpdate(individualDTO, checkPeselDuplication, false);
        individualRepository.get(individualId);


        //CREATE CONTRACT
        ContractDTO contractDTO = createContractDTO(importComplexContractDTO, paramsDTO,
                contractType,
                individualId, enterpriseId, individualOrEnterpriseAccountContractPair,
                zipCodeIndividualInvoice, zipCodeEnterpriseInvoice);

        contractTypeRepository.flush();
        contractService.saveContract(contractDTO);

        //CREATE ORDER
        Contract contract = contractRepository.get(contractId);
        CreateOrderDTO orderDTO = createCreateOrderDTO(importComplexContractDTO.getContract(), contract);
        orderId = orderService.createOrder(orderDTO);

        ImportResultDTO result = new ImportResultDTO();
        result.setContractId(contractId);
        result.setEnterpriseId(enterpriseId);
        result.setIndividualId(individualId);
        result.setOrderId(orderId);

        return result;
    }

    private void validateUsageContractID(String contractId, AccountContractPair accountContractPair) {
        if (Objects.isNull(accountContractPair)) {
             accountContractPair = accountContractPairService.findByContractId(contractId);
        }
        List<EntityConstraintViolation> violations = Collections.emptyList();
        String message = "";
        if (accountContractPair == null) {
            message = "Podane id umowy nie wystepuje w bazie";
            violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
            return;
        }
        if (accountContractPair.isUsed()) {
            message = "Para id umowy - subkonto istnieje, ale jest ju?? u??yte.";
            violations.add(new EntityConstraintViolation(Contract.ID_ATTR_NAME, message, null));
            return;
        }

    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportComplexContractDTO createComplexContractDTO(ImportParamsDTO paramsDTO, Row row){
        ImportComplexContractDTO c = new ImportComplexContractDTO();

        //Ustaw Program
        c.getContract().setGrantProgram(paramsDTO.getGrantProgram());

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
                    c.getContract().setContractTrainingCategories(getStringListCellValue(cell));
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
                    // Todo: column with phone number
                    break;
                case 16:
                    c.getEnterprise().setName(getStringCellValue(cell));
                    break;
                case 17:
                    c.getEnterprise().setVatRegNum(getStringCellValue(cell));
                    break;
                case 18:
                    c.getEnterprise().getAddressInvoice().setStreet(getStringCellValue(cell));
                    break;
                case 19:
                    c.getEnterprise().getAddressInvoice().setHomeNumber(getStringCellValue(cell));
                    break;
                case 20:
                    c.getEnterprise().getAddressInvoice().setFlatNumber(getStringCellValue(cell));
                    break;
                case 21:
                    c.getEnterprise().getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 22:
                    c.getEnterprise().getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 23:
                    c.getEnterprise().setEmail(getStringCellValue(cell));
                    break;
                case 24 :
                    c.getContract().setOwnContributionPercentage(getBigDecimalCellValue(cell));
            }
        }
        return c;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private EnterpriseDto createEnterpriseDTO(ImportEnterpriseDTO importDTO, ZipCode zipCodeEnterpriseInvoice){
        EnterpriseDto dto = new EnterpriseDto();

        dto.setAccountRepayment(null);

        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(!importDTO.getAddressInvoice().isEmpty()) {
            ImportAddressInvoiceSplitDTO address = importDTO.getAddressInvoice();
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

    private IndividualDto createIndividualDTO(ImportIndividualDTO importDTO, ZipCode zipCodeIndividualInvoice, Long enterpriseId){
        IndividualDto dto = new IndividualDto();

        dto.setAccountRepayment(null);

        dto.setFirstName(importDTO.getFirstName());
        dto.setLastName(importDTO.getLastName());
        dto.setPesel(importDTO.getPesel());

        dto.setSex(createSex(importDTO));
        dto.setDocumentType(null);
        dto.setDocumentNumber(null);
        if(!importDTO.getAddressInvoice().isEmpty()) {
            ImportAddressInvoiceSplitDTO address = importDTO.getAddressInvoice();
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

    @Override
    public void saveData(Long importJobId, ImportParamsDTO paramsDTO, Row row) {
        super.saveData(importJobId, paramsDTO, row);
    }

    private ContractDTO createContractDTO(ImportComplexContractDTO importComplexContractDTO, ImportParamsDTO paramsDTO,
                                          ContractType contractType, Long individualId, Long enterpriseId, AccountContractPair pair, ZipCode zipCodeIndividualInvoice, ZipCode zipCodeEnterpriseInvoice){

        ImportContractDTO importContractDTO = importComplexContractDTO.getContract();

        //New contract
        ContractDTO dto = new ContractDTO();
        dto.setId(importContractDTO.getId(identityGeneratorService.getGenerator(importContractDTO)));
        dto.setSignDate(importContractDTO.getSignDate());
        dto.setExpiryDate(importContractDTO.getExpiryDate());
        dto.setTrainingCategory(importContractDTO.getContractTrainingCategories());
        dto.setContractType(new DictionaryDTO(importContractDTO.getContractType()));

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

        dto.setCode(pair != null ? accountContractPairService.getCodeFromAccount(pair) : null);
        dto.setAccountPayment(pair != null ? pair.getAccountPayment() : null);

        if(ContractType.TYPE_ENT.equals(contractType.getId())){
            ImportEnterpriseDTO importEnterpriseDTO = importComplexContractDTO.getEnterprise();
            setFullAddress(dto, importEnterpriseDTO.getAddressInvoice(), zipCodeEnterpriseInvoice);
        } else if (ContractType.TYPE_IND.equals(contractType.getId())){
            ImportIndividualDTO importIndividualDTO = importComplexContractDTO.getIndividual();
            setFullAddress(dto, importIndividualDTO.getAddressInvoice(), zipCodeIndividualInvoice);
        }

        return dto;
    }

    private void setFullAddress(ContractDTO dto, ImportAddressInvoiceSplitDTO addressInvoice, ZipCode zipCodeEnterpriseInvoice) {
        if(!addressInvoice.isEmpty()) {
            dto.setAddressInvoice(addressInvoice.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(zipCodeEnterpriseInvoice));
            dto.setAddressCorr(addressInvoice.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(zipCodeEnterpriseInvoice));
        }
    }

    private CreateOrderDTO createCreateOrderDTO(ImportContractDTO importDTO, Contract contract){
        CreateOrderDTO dto = orderServiceLocal.createCreateOrderDTO(contract);
        dto.setExternalOrderId(importDTO.getExternalOrderId());
        dto.setOrderDate(importDTO.getSignDate());
        dto.setProductInstanceNum(importDTO.getProductInstanceNum());
        dto.setOwnContributionPercent(importDTO.getOwnContributionPercentage());
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

    /**
     * Set percentage of own contribution for contract.
     * If not exists in file, value is get from default parameter.
     *
     * @param importComplexContractDTO contract import dto
     */
    private void setDefaultOwnContributionPercentage(ImportComplexContractDTO importComplexContractDTO, ImportParamsDTO importParamsDTO) {
        BigDecimal ownContributionPercentageFromFile = importComplexContractDTO.getContract().getOwnContributionPercentage();

        if(ownContributionPercentageFromFile == null){
            GrantProgramParam grantProgramParam = paramInDateService.findGrantProgramParam(importParamsDTO.getGrantProgramId(), GrantProgramParam.OWN_CONTRIBUTION_PERCENT_DEFAULT, new Date(), false);
            if (grantProgramParam != null && grantProgramParam.getValue() != null) {
                importComplexContractDTO.getContract().setOwnContributionPercentage(new BigDecimal(grantProgramParam.getValue()));
            }
        }
    }

    /**
     * Create additional description for imported row.
     * @param importComplexContractDTO all informations about imported contract
     * @param paramsDTO import parameters
     * @return additional description
     */
    private String getAdditionalDescription(ImportComplexContractDTO importComplexContractDTO, ImportParamsDTO paramsDTO) {
        return validateOwnContributionImportWithParam(importComplexContractDTO, paramsDTO);
    }

    /**
     * Check own contribution percentage from file with parameters if they're exists. If parameters exists with other values, then add description with information about this.
     * @param importComplexContractDTO all informations about imported contract
     * @param paramsDTO import parameters
     * @return additional description for own contribution
     */
    private String validateOwnContributionImportWithParam(ImportComplexContractDTO importComplexContractDTO, ImportParamsDTO paramsDTO) {
        String result = "";
        BigDecimal ownContributionPercentage = importComplexContractDTO.getContract().getOwnContributionPercentage();
        List<GrantProgramParam> params = grantProgramParamRepository.findByGrantProgramInDate(paramsDTO.getGrantProgramId(), GrantProgramParam.OWN_CONTRIBUTION_PERCENT_IMPORT_INFO, importComplexContractDTO.getContract().getSignDate());

        if (params.size() > 0 && ownContributionPercentage != null) {
            GrantProgramParam grantProgramParam = params.stream()
                    .filter(param -> (ownContributionPercentage.compareTo(new BigDecimal(param.getValue())) == 0))
                    .findAny()
                    .orElse(null);
            if (grantProgramParam == null) {
                result = "Uwaga! Rozbie??no???? finansowa procentu kwoty wk??adu w??asnego. ";
            }
        }

        return result;
    }

}
