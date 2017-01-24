package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import lombok.Getter;
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
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TrainingInstitutionContactEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TrainingInstitutionUserEntityMapper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importTrainingInstitutionService")
public class ImportTrainingInstitutionServiceImpl extends ImportBaseDataServiceImpl {

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

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private VerificationService verificationService;

    //OVERRIDE

    @Override
    protected ImportResultDTO saveInternalImportDataRowBeforeSaveData(ImportParamsDTO paramsDTO, Row row){
        String externalId = getExternalId(row);
        TrainingInstitution trainingInstitution = externalId != null ? trainingInstitutionRepository.findByExternalId(externalId) : null;

        ImportResultDTO result = new ImportResultDTO();
        result.setTrainingInstitutionId(trainingInstitution != null ? trainingInstitution.getId() : null);
        return result;
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportTrainingInstitutionDTO importDTO = createImportDTO(row);
        validateImport(importDTO);

        GrantProgram grantProgram = grantProgramRepository.get(paramsDTO.getGrantProgramId());
        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getExternalId());
        TrainingInstitutionUser tiUser = trainingInstitutionUserDao.findByLoginIgnoreCase(importDTO.getEmail());
        ZipCode zipCodeInvoice = zipCodeRepository.findActiveByCode(importDTO.getAddressInvoice().getZipCode());
        ZipCode zipCodeCorr = zipCodeRepository.findActiveByCode(importDTO.getAddressCorr().getZipCode());
        validateConnectedData(importDTO, trainingInstitution, zipCodeInvoice, zipCodeCorr, tiUser);

        ImportResultDTO result = new ImportResultDTO();
        TrainingInstitutionTempDto tempDto = createTrainingInstitutionDTO(trainingInstitution, importDTO, zipCodeInvoice, zipCodeCorr);

        if(trainingInstitution == null){
            Long trainingInstitutionId = trainingInstitutionService.saveTrainingInstitution(tempDto.getTrainingInstitution(), false);
            result.setTrainingInstitutionId(trainingInstitutionId);
            result.setDescrption(String.format("Poprawnie utworzono dane: instytucje szkoleniową (%s).", getIdToDescription(trainingInstitutionId)));
        }else{
            trainingInstitutionService.updateTrainingInstitution(tempDto.getTrainingInstitution(), false);
            result.setTrainingInstitutionId(tempDto.getTrainingInstitution().getId());
            result.setDescrption(String.format("Poprawnie zaktualizowano dane: instytucje szkoleniową (%s).", tempDto.getTrainingInstitution().getId()));
        }

        if(tempDto.isSendMail()){
            sendTiAccessMail(grantProgram, importDTO.getEmail());
            result.setDescrption(result.getDescrption() + String.format(" Wysłano maila powitalnego na adres %s.", importDTO.getEmail()));
        }
        return result;
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportTrainingInstitutionDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);
        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportTrainingInstitutionDTO importDTO, TrainingInstitution trainingInstitution,
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

    private ImportTrainingInstitutionDTO createImportDTO(Row row){
        ImportTrainingInstitutionDTO ti = new ImportTrainingInstitutionDTO();

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
                    ti.getAddressInvoice().setAddress(getStringCellValue(cell));
                    break;
                case 4:
                    ti.getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 5:
                    ti.getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 6:
                    ti.getAddressCorr().setAddress(getStringCellValue(cell));
                    break;
                case 7:
                    ti.getAddressCorr().setZipCode(getStringCellValue(cell));
                    break;
                case 8:
                    ti.getAddressCorr().setCity(getStringCellValue(cell));
                    break;
                case 9:
                    ti.setEmail(getStringCellValue(cell));
                    break;
            }
        }
        return ti;
    }

    private String getExternalId(Row row){
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    return getStringCellValue(cell);
            }
        }
        return null;
    }


    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingInstitutionTempDto createTrainingInstitutionDTO(TrainingInstitution trainingInstitution, final ImportTrainingInstitutionDTO importDTO,
                                                                ZipCode zipCodeInvoice, ZipCode zipCodeCorr){
        TrainingInstitutionDto dto = new TrainingInstitutionDto();
        boolean sendMail = false;

        dto.setId(trainingInstitution != null ? trainingInstitution.getId() : null);
        dto.setExternalId(importDTO.getExternalId());
        dto.setCode(trainingInstitution != null ? trainingInstitution.getCode(): null);
        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(importDTO.getAddressInvoice() != null) {
            ImportAddressInvoiceConcatDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(zipCodeInvoice));
        }
        if(importDTO.getAddressCorr() != null) {
            ImportAddressCorrConcatDTO address = importDTO.getAddressCorr();
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(zipCodeCorr));
        }

        dto.setRemarks(trainingInstitution != null ? trainingInstitution.getRemarks(): null);

        //USER & CONTACTS
        if(trainingInstitution == null) {
            dto.setUsers(Lists.newArrayList(createGryfTiUserDto(importDTO)));
            dto.setContacts(Lists.newArrayList(createTrainingInstitutionContactDto(importDTO)));
            sendMail = true;
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
                sendMail = true;
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
        return new TrainingInstitutionTempDto(dto, sendMail);
    }

    private GryfTiUserDto createGryfTiUserDto(ImportTrainingInstitutionDTO importDTO){
        GryfTiUserDto user = new GryfTiUserDto();
        user.setLogin(importDTO.getEmail());
        user.setEmail(importDTO.getEmail());
        RoleDto roleDTO = new RoleDto();
        roleDTO.setCode(Privileges.TI_MAIN_ROLE.name());
        user.setRoles(Lists.newArrayList(roleDTO));
        return user;
    }

    private TrainingInstitutionContactDto createTrainingInstitutionContactDto(ImportTrainingInstitutionDTO importDTO){
        TrainingInstitutionContactDto contactDTO = new TrainingInstitutionContactDto();
        contactDTO.setContactType(new ContactTypeDto());
        contactDTO.getContactType().setType(ContactType.TYPE_EMAIL);
        contactDTO.setContactData(importDTO.getEmail());
        return contactDTO;
    }

    private void sendTiAccessMail(GrantProgram grantProgram, String email){
        verificationService.sendTiUserAccess(grantProgram.getProgramName(), email);
    }

    //CLASSES

    private class TrainingInstitutionTempDto{

        @Getter
        private TrainingInstitutionDto trainingInstitution;

        @Getter
        private boolean sendMail;

        public TrainingInstitutionTempDto(TrainingInstitutionDto trainingInstitution, boolean sendMail){
            this.trainingInstitution = trainingInstitution;
            this.sendMail = sendMail;
        }
    }

}
