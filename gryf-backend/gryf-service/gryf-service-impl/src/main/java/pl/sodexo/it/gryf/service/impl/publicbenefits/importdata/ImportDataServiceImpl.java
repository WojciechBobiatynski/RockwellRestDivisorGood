package pl.sodexo.it.gryf.service.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.enums.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Isolution on 2016-11-30.
 */
@Service
@Transactional
public class ImportDataServiceImpl implements ImportDataService{

    @Autowired
    private FileService fileService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private AccountContractPairService accountContractPairService;

    @Override
    public void test() {
        try {
            InputStream is = fileService.getInputStream("C:\\Users\\Isolution\\Desktop\\GryfWorkspace\\GryfFileRepo\\import\\import-umowy.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            System.out.println(sheet.getPhysicalNumberOfRows());

            while(rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if(row.getRowNum() != 0){

                    ImportComplexContractDTO dto = createComplexContractDTO(row);
                    System.out.println(dto);
                    saveContractData(dto);
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContractData(ImportComplexContractDTO importDTO){
        Long contractId = importDTO.getContract().getId();
        Long enterpriseId;
        Long individualId;

        try {
            EnterpriseDto enterpriseDTO = createEnterpriseDTO(importDTO.getEnterprise());
            enterpriseId = enterpriseService.saveEnterpriseDto(enterpriseDTO, false);

            IndividualDto individualDTO = createIndividualDTO(importDTO.getIndividual(), contractId, enterpriseId);
            individualId = individualService.saveIndividual(individualDTO, true, false);

            ContractDTO contract = createContractDTO(importDTO.getContract(), individualId, enterpriseId);
            contractService.saveContract(contract);
        }catch (EntityValidationException e){
            for(EntityConstraintViolation v : e.getViolations()){
                System.out.println(v.getMessage());
            }
        }
    }

    private ImportComplexContractDTO createComplexContractDTO(Row row){
        ImportComplexContractDTO c = new ImportComplexContractDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    c.setContract(new ImportContractDTO());
                    c.getContract().setId((long) cell.getNumericCellValue());
                    break;
                case 1:
                    c.getContract().setGrantProgramId((long) cell.getNumericCellValue());
                    break;
                case 2:
                    c.getContract().setContractType(cell.getStringCellValue());
                    break;
                case 3:
                    c.getContract().setSignDate(cell.getDateCellValue());
                    break;
                case 4:
                    c.getContract().setExpiryDate(cell.getDateCellValue());
                    break;
                case 5:
                    c.getContract().setContractTrainingCategories(cell.getStringCellValue());
                    break;
                case 6:
                    c.setIndividual(new ImportIndividualDTO());
                    c.getIndividual().setFirstName(cell.getStringCellValue());
                    break;
                case 7:
                    c.getIndividual().setLastName(cell.getStringCellValue());
                    break;
                case 8:
                    c.getIndividual().setPesel(cell.getStringCellValue());
                    break;
                case 9:
                    c.getIndividual().setAddressInvoice(new ImportAddressDTO());
                    c.getIndividual().getAddressInvoice().setAddress(cell.getStringCellValue());
                    break;
                case 10:
                    c.getIndividual().getAddressInvoice().setZipCode(cell.getStringCellValue());
                    break;
                case 11:
                    c.getIndividual().getAddressInvoice().setCity(cell.getStringCellValue());
                    break;
                case 12:
                    c.getIndividual().setAddressCorr(new ImportAddressDTO());
                    c.getIndividual().getAddressCorr().setAddress(cell.getStringCellValue());
                    break;
                case 13:
                    c.getIndividual().getAddressCorr().setZipCode(cell.getStringCellValue());
                    break;
                case 14:
                    c.getIndividual().getAddressCorr().setCity(cell.getStringCellValue());
                    break;
                case 15:
                    c.getIndividual().setEmail(cell.getStringCellValue());
                    break;
                case 16:
                    c.getIndividual().setBankAccount(cell.getStringCellValue());
                    break;
                case 17:
                    c.setEnterprise(new ImportEnterpriseDTO());
                    c.getEnterprise().setName(cell.getStringCellValue());
                    break;
                case 18:
                    c.getEnterprise().setVatRegNum(cell.getStringCellValue());
                    break;
                case 19:
                    c.getEnterprise().setAddressInvoice(new ImportAddressDTO());
                    c.getEnterprise().getAddressInvoice().setAddress(cell.getStringCellValue());
                    break;
                case 20:
                    c.getEnterprise().getAddressInvoice().setZipCode(cell.getStringCellValue());
                    break;
                case 21:
                    c.getEnterprise().getAddressInvoice().setCity(cell.getStringCellValue());
                    break;
                case 22:
                    c.getEnterprise().setAddressCorr(new ImportAddressDTO());
                    c.getEnterprise().getAddressCorr().setAddress(cell.getStringCellValue());
                    break;
                case 23:
                    c.getEnterprise().getAddressCorr().setZipCode(cell.getStringCellValue());
                    break;
                case 24:
                    c.getEnterprise().getAddressCorr().setCity(cell.getStringCellValue());
                    break;
                case 25:
                    c.getEnterprise().setBankAccount(cell.getStringCellValue());
                    break;
            }
        }

        return c;
    }

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
        dto.setAccountRepayment(importDTO.getBankAccount());
        return dto;
    }

    private IndividualDto createIndividualDTO(ImportIndividualDTO importDTO, Long contractId, Long enterpriseId){
        IndividualDto dto = new IndividualDto();

        AccountContractPair pair = accountContractPairService.getValidAccountContractPairForUsed(contractId);
        dto.setCode(accountContractPairService.getCodeFromAccount(pair));
        dto.setAccountPayment(pair.getAccountPayment());

        dto.setAccountRepayment(importDTO.getBankAccount());
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

        EnterpriseDto enterpriseDTO = new EnterpriseDto();
        enterpriseDTO.setId(enterpriseId);
        dto.setEnterprises(Lists.newArrayList(enterpriseDTO));

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

        EnterpriseSearchResultDTO enterprise  = new EnterpriseSearchResultDTO();
        enterprise.setId(enterpriseId);
        dto.setEnterprise(enterprise);

        return dto;
    }

    private ZipCodeDto createZipCodeDTO(ImportAddressDTO address){
        ZipCode zipCode = zipCodeRepository.findActiveByCode(address.getZipCode());
        if(zipCode != null){
            ZipCodeDto dto = new ZipCodeDto();
            dto.setId(zipCode.getId());
            return dto;
        }
        return null;
    }

    private String createSex(ImportIndividualDTO importDTO){
        return PeselUtils.isMale(importDTO.getPesel()) ? Sex.M.name() : Sex.K.name();
    }
}
