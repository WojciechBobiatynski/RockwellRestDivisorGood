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
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.enums.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.Iterator;
import java.util.List;

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
    private AccountContractPairService accountContractPairService;

    @Autowired
    private GryfValidator gryfValidator;

    //OVERRIDE

    @Override
    protected String saveData(Row row){
        ImportComplexContractDTO dto = createComplexContractDTO(row);
        validateImport(dto);
        ImportComplexContractResultDTO importResult = saveContractData(dto);
        return String.format("Poprawno zapisano dane: umowa (%s) użytkownik (%s), MŚP (%s)",
                getIdToDescription(importResult.getContractId()), getIdToDescription(importResult.getIndividualId()),
                getIdToDescription(importResult.getEnterpriseId()));
    }

    //PRIVATE METHODS - SAVE

    private void validateImport(ImportComplexContractDTO dto){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(dto);

        if(ContractType.TYPE_ENT.equals(dto.getContract().getContractType())){
            violations.addAll(gryfValidator.generateViolation(dto.getEnterprise()));
        }else{
            if(!dto.getEnterprise().isEmpty()){
                violations.add(new EntityConstraintViolation("Dla typu kontraktu 'IND' (osoba fizyczna) pola dla MŚP powinny być puste."));
            }
        }

        gryfValidator.validate(violations);
    }

    private ImportComplexContractResultDTO saveContractData(ImportComplexContractDTO importDTO){

        Long contractId = importDTO.getContract().getId();
        Long enterpriseId = null;
        Long individualId = null;

        if(ContractType.TYPE_ENT.equals(importDTO.getContract().getContractType())){
            EnterpriseDto enterpriseDTO = createEnterpriseDTO(importDTO.getEnterprise());
            enterpriseId = enterpriseService.saveEnterpriseDto(enterpriseDTO, false, false);
        }


        IndividualDto individualDTO = createIndividualDTO(importDTO.getIndividual(), contractId, enterpriseId);
        individualId = individualService.saveIndividual(individualDTO, true, false);

        ContractDTO contract = createContractDTO(importDTO.getContract(), individualId, enterpriseId);
        contractService.saveContract(contract);

        ImportComplexContractResultDTO result = new ImportComplexContractResultDTO();
        result.setContractId(contractId);
        result.setEnterpriseId(enterpriseId);
        result.setIndividualId(individualId);
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
                    c.setContract(new ImportContractDTO());
                    c.getContract().setId(getLongCellValue(cell));
                    break;
                case 1:
                    c.getContract().setGrantProgramId(getLongCellValue(cell));
                    break;
                case 2:
                    c.getContract().setContractType(getStringCellValue(cell));
                    break;
                case 3:
                    c.getContract().setSignDate(getDateCellValue(cell));
                    break;
                case 4:
                    c.getContract().setExpiryDate(getDateCellValue(cell));
                    break;
                case 5:
                    c.getContract().setContractTrainingCategories(getStringCellValue(cell));
                    break;
                case 6:
                    c.setIndividual(new ImportIndividualDTO());
                    c.getIndividual().setFirstName(getStringCellValue(cell));
                    break;
                case 7:
                    c.getIndividual().setLastName(getStringCellValue(cell));
                    break;
                case 8:
                    c.getIndividual().setPesel(getStringCellValue(cell));
                    break;
                case 9:
                    c.getIndividual().setAddressInvoice(new ImportAddressDTO());
                    c.getIndividual().getAddressInvoice().setAddress(getStringCellValue(cell));
                    break;
                case 10:
                    c.getIndividual().getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 11:
                    c.getIndividual().getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 12:
                    c.getIndividual().setAddressCorr(new ImportAddressDTO());
                    c.getIndividual().getAddressCorr().setAddress(getStringCellValue(cell));
                    break;
                case 13:
                    c.getIndividual().getAddressCorr().setZipCode(getStringCellValue(cell));
                    break;
                case 14:
                    c.getIndividual().getAddressCorr().setCity(getStringCellValue(cell));
                    break;
                case 15:
                    c.getIndividual().setEmail(getStringCellValue(cell));
                    break;
                case 16:
                    c.setEnterprise(new ImportEnterpriseDTO());
                    c.getEnterprise().setName(getStringCellValue(cell));
                    break;
                case 17:
                    c.getEnterprise().setVatRegNum(getStringCellValue(cell));
                    break;
                case 18:
                    c.getEnterprise().setAddressInvoice(new ImportAddressDTO());
                    c.getEnterprise().getAddressInvoice().setAddress(getStringCellValue(cell));
                    break;
                case 19:
                    c.getEnterprise().getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 20:
                    c.getEnterprise().getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 21:
                    c.getEnterprise().setAddressCorr(new ImportAddressDTO());
                    c.getEnterprise().getAddressCorr().setAddress(getStringCellValue(cell));
                    break;
                case 22:
                    c.getEnterprise().getAddressCorr().setZipCode(getStringCellValue(cell));
                    break;
                case 23:
                    c.getEnterprise().getAddressCorr().setCity(getStringCellValue(cell));
                    break;
                case 24:
                    c.getEnterprise().setEmail(getStringCellValue(cell));
                    break;
            }
        }
        return c;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private EnterpriseDto createEnterpriseDTO(ImportEnterpriseDTO importDTO){
        EnterpriseDto dto = new EnterpriseDto();

        dto.setCode(null);
        dto.setAccountPayment(null);
        dto.setAccountRepayment(null);
        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(importDTO.getAddressInvoice() != null) {
            ImportAddressDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(address));
        }
        if(importDTO.getAddressCorr() != null) {
            ImportAddressDTO address = importDTO.getAddressCorr();
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(address));
        }

        dto.setContacts(Lists.newArrayList());
        return dto;
    }

    private IndividualDto createIndividualDTO(ImportIndividualDTO importDTO, Long contractId, Long enterpriseId){
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
        if(importDTO.getAddressInvoice() != null) {
            ImportAddressDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(address));
        }
        if(importDTO.getAddressCorr() != null) {
            ImportAddressDTO address = importDTO.getAddressInvoice();
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(address));
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

    private ContractDTO createContractDTO(ImportContractDTO importDTO, Long individualId, Long enterpriseId){
        ContractDTO dto = new ContractDTO();
        dto.setId(importDTO.getId());
        dto.setSignDate(importDTO.getSignDate());
        dto.setExpiryDate(importDTO.getSignDate());
        dto.setTrainingCategory(Lists.newArrayList(importDTO.getContractTrainingCategories().split(",")));
        dto.setContractType(new DictionaryDTO(importDTO.getContractType()));

        GrantProgramDictionaryDTO grantProgram = new GrantProgramDictionaryDTO();
        grantProgram.setId(importDTO.getGrantProgramId());
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

    private String createSex(ImportIndividualDTO importDTO){
        return PeselUtils.isMale(importDTO.getPesel()) ? Sex.M.name() : Sex.K.name();
    }

}
