package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions.TrainingInstitutionUserDao;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TrainingInstitutionContactEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TrainingInstitutionUserEntityMapper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2017-01-12.
 */
@Deprecated()
@Service(value = "importTrainingInstitutionV1Service")
public class ImportTrainingInstitutionV1ServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private TrainingInstitutionUserDao trainingInstitutionUserDao;

    @Autowired
    private TrainingInstitutionUserEntityMapper trainingInstitutionUserEntityMapper;

    @Autowired
    private TrainingInstitutionContactEntityMapper trainingInstitutionContactEntityMapper;

    //OVERRIDE

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportTrainingInstitutionV1DTO importDTO = createImportDTO(row);
        validateImport(importDTO);


        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getExternalId());
        TrainingInstitutionUser tiUser = trainingInstitutionUserDao.findByLoginIgnoreCase(importDTO.getEmail());
        ZipCode zipCodeInvoice = zipCodeRepository.findActiveByCode(importDTO.getAddressInvoice().getZipCode());
        ZipCode zipCodeCorr = zipCodeRepository.findActiveByCode(importDTO.getAddressCorr().getZipCode());
        validateConnectedData(importDTO, trainingInstitution, zipCodeInvoice, zipCodeCorr, tiUser);

        TrainingInstitutionDto trainingInstitutionDto = createTrainingInstitutionDTO(trainingInstitution, importDTO, zipCodeInvoice, zipCodeCorr);
        if(trainingInstitution == null){
            Long trainingInstitutionId = trainingInstitutionService.saveTrainingInstitution(trainingInstitutionDto, false);

            ImportResultDTO result = new ImportResultDTO();
            result.setTrainingInstitutionId(trainingInstitutionId);
            result.setDescrption(String.format("Poprawno utworzono dane: instytucje szkoleniową (%s)", getIdToDescription(trainingInstitutionId)));
            return result;

        }else{
            trainingInstitutionService.updateTrainingInstitution(trainingInstitutionDto, false);

            ImportResultDTO result = new ImportResultDTO();
            result.setTrainingInstitutionId(trainingInstitutionDto.getId());
            result.setDescrption(String.format("Poprawno zaktualizowano dane: instytucje szkoleniową (%s)", trainingInstitutionDto.getId()));
            return result;
        }
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportTrainingInstitutionV1DTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);
        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportTrainingInstitutionV1DTO importDTO, TrainingInstitution trainingInstitution,
                                    ZipCode zipCodeInvoice, ZipCode zipCodeCorr, TrainingInstitutionUser tiUser){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(zipCodeInvoice == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla adresu do faktury "
                    + "o wartości (%s)", importDTO.getAddressInvoice().getZipCode())));
        }
        if(zipCodeCorr == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kodu pocztowego dla adresu korespondencyjnego "
                    + "o wartości (%s)", importDTO.getAddressCorr().getZipCode())));
        }
        if(tiUser != null){
            if(trainingInstitution == null) {
                violations.add(new EntityConstraintViolation(
                        String.format("Email (%s) jest już używany jako login użytkonika TI. Użytkownik o danym emailu "
                                        + "istnieje w Usługodawcy o identyfikatorze (%s).", importDTO.getEmail(),
                                tiUser.getTrainingInstitution().getId())));
            }else if(!tiUser.getTrainingInstitution().equals(trainingInstitution)){
                violations.add(new EntityConstraintViolation(
                        String.format("Email (%s) jest już używany jako login użytkonika TI. Użytkownik o danym emailu "
                                        + "istnieje w Usługodawcy o identyfikatorze (%s).", importDTO.getEmail(),
                                tiUser.getTrainingInstitution().getId())));
            }
        }

        gryfValidator.validate(violations);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportTrainingInstitutionV1DTO createImportDTO(Row row){
        ImportTrainingInstitutionV1DTO ti = new ImportTrainingInstitutionV1DTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    ti.setExternalId(getStringCellValue(cell));
                    break;
                case 1:
                    ti.setVatRegNum(getStringCellValue(cell));
                    break;
                case 2:
                    ti.setName(getStringCellValue(cell));
                    break;
                case 3:
                    ti.getAddressInvoice().setStreet(getStringCellValue(cell));
                    break;
                case 4:
                    ti.getAddressInvoice().setHomeNumber(getStringCellValue(cell));
                    break;
                case 5:
                    ti.getAddressInvoice().setFlatNumber(getStringCellValue(cell));
                    break;
                case 6:
                    ti.getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 7:
                    ti.getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 8:
                    ti.getAddressCorr().setStreet(getStringCellValue(cell));
                    break;
                case 9:
                    ti.getAddressCorr().setHomeNumber(getStringCellValue(cell));
                    break;
                case 10:
                    ti.getAddressCorr().setFlatNumber(getStringCellValue(cell));
                    break;
                case 11:
                    ti.getAddressCorr().setZipCode(getStringCellValue(cell));
                    break;
                case 12:
                    ti.getAddressCorr().setCity(getStringCellValue(cell));
                    break;
                case 13:
                    ti.setEmail(getStringCellValue(cell));
                    break;
            }
        }
        return ti;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingInstitutionDto createTrainingInstitutionDTO(TrainingInstitution trainingInstitution, final ImportTrainingInstitutionV1DTO importDTO,
            ZipCode zipCodeInvoice, ZipCode zipCodeCorr){
        TrainingInstitutionDto dto = new TrainingInstitutionDto();
        dto.setId(trainingInstitution != null ? trainingInstitution.getId() : null);
        dto.setExternalId(importDTO.getExternalId());
        dto.setCode(trainingInstitution != null ? trainingInstitution.getCode(): null);
        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(importDTO.getAddressInvoice() != null) {
            ImportAddressInvoiceSplitDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(zipCodeInvoice));
        }
        if(importDTO.getAddressCorr() != null) {
            ImportAddressCorrSplitDTO address = importDTO.getAddressCorr();
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(zipCodeCorr));
        }

        dto.setRemarks(trainingInstitution != null ? trainingInstitution.getRemarks(): null);

        //USER & CONTACTS
        if(trainingInstitution == null) {
            dto.setUsers(Lists.newArrayList(createGryfTiUserDto(importDTO)));
            dto.setContacts(Lists.newArrayList(createTrainingInstitutionContactDto(importDTO)));
        }else{

            //USER
            dto.setUsers(trainingInstitutionUserEntityMapper.convert(trainingInstitution.getTrainingInstitutionUsers()));
            boolean userExist = GryfUtils.contain(trainingInstitution.getTrainingInstitutionUsers(), new GryfUtils.Predicate<TrainingInstitutionUser>() {
                public boolean apply(TrainingInstitutionUser input) {
                    return input.getEmail().equals(importDTO.getEmail());
                }
            });
            if(!userExist){
                dto.getUsers().add(createGryfTiUserDto(importDTO));
            }

            //CONTACT
            dto.setContacts(trainingInstitutionContactEntityMapper.convert(trainingInstitution.getContacts()));
            boolean contactExist = GryfUtils.contain(trainingInstitution.getContacts(), new GryfUtils.Predicate<TrainingInstitutionContact>() {
                public boolean apply(TrainingInstitutionContact input) {
                    return ContactType.TYPE_EMAIL.equals(input.getContactType().getType()) &&
                            importDTO.getEmail().equals(input.getContactData());
                }
            });
            if(!contactExist){
                dto.getContacts().add(createTrainingInstitutionContactDto(importDTO));
            }
        }

        dto.setVersion(trainingInstitution != null ? trainingInstitution.getVersion() : null);
        dto.setCreatedUser(trainingInstitution != null ? trainingInstitution.getCreatedUser() : null);
        dto.setCreatedTimestamp(trainingInstitution != null ? trainingInstitution.getCreatedTimestamp() : null);
        dto.setModifiedUser(trainingInstitution != null ? trainingInstitution.getModifiedUser() : null);
        dto.setModifiedTimestamp(trainingInstitution != null ? trainingInstitution.getModifiedTimestamp() : null);
        return dto;
    }

    private GryfTiUserDto createGryfTiUserDto(ImportTrainingInstitutionV1DTO importDTO){
        GryfTiUserDto user = new GryfTiUserDto();
        user.setLogin(importDTO.getEmail());
        user.setEmail(importDTO.getEmail());
        RoleDto roleDTO = new RoleDto();
        roleDTO.setCode(Privileges.TI_MAIN_ROLE.name());
        user.setRoles(Lists.newArrayList(roleDTO));
        return user;
    }

    private TrainingInstitutionContactDto createTrainingInstitutionContactDto(ImportTrainingInstitutionV1DTO importDTO){
        TrainingInstitutionContactDto contactDTO = new TrainingInstitutionContactDto();
        contactDTO.setContactType(new ContactTypeDto());
        contactDTO.getContactType().setType(ContactType.TYPE_EMAIL);
        contactDTO.setContactData(importDTO.getEmail());
        return contactDTO;
    }

}
