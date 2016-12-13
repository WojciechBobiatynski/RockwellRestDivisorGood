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
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

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

    //OVERRIDE

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportTrainingInstitutionDTO importDTO = createImportDTO(row);
        validateImport(importDTO);

        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getExternalId());

        TrainingInstitutionDto trainingInstitutionDto = createTrainingInstitutionDTO(trainingInstitution, importDTO);
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

    private void validateImport(ImportTrainingInstitutionDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);
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

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingInstitutionDto createTrainingInstitutionDTO(TrainingInstitution trainingInstitution,
                                                                ImportTrainingInstitutionDTO importDTO){
        TrainingInstitutionDto dto = new TrainingInstitutionDto();
        dto.setId(trainingInstitution != null ? trainingInstitution.getId() : null);
        dto.setExternalId(importDTO.getExternalId());
        dto.setCode(null);
        dto.setName(importDTO.getName());
        dto.setVatRegNum(importDTO.getVatRegNum());

        if(importDTO.getAddressInvoice() != null) {
            ImportAddressInvoiceDTO address = importDTO.getAddressInvoice();
            dto.setAddressInvoice(address.getAddress());
            dto.setZipCodeInvoice(createZipCodeDTO(address));
        }
        if(importDTO.getAddressCorr() != null) {
            ImportAddressCorrDTO address = importDTO.getAddressCorr();
            dto.setAddressCorr(address.getAddress());
            dto.setZipCodeCorr(createZipCodeDTO(address));
        }

        dto.setRemarks(null);

        TrainingInstitutionContactDto contactDTO = new TrainingInstitutionContactDto();
        contactDTO.setContactType(new ContactTypeDto());
        contactDTO.getContactType().setType(ContactType.TYPE_EMAIL);
        contactDTO.setContactData(importDTO.getEmail());
        dto.setContacts(Lists.newArrayList(contactDTO));

        if(trainingInstitution == null) {
            GryfTiUserDto user = new GryfTiUserDto();
            user.setLogin(importDTO.getEmail());
            user.setEmail(importDTO.getEmail());
            RoleDto roleDTO = new RoleDto();
            roleDTO.setCode(Privileges.TI_MAIN_ROLE.name());
            user.setRoles(Lists.newArrayList(roleDTO));
            dto.setUsers(Lists.newArrayList(user));
        }else{
            dto.setUsers(Lists.newArrayList());
        }

        dto.setVersion(trainingInstitution != null ? trainingInstitution.getVersion() : null);
        dto.setCreatedUser(trainingInstitution != null ? trainingInstitution.getCreatedUser() : null);
        dto.setCreatedTimestamp(trainingInstitution != null ? trainingInstitution.getCreatedTimestamp() : null);
        return dto;
    }

}
