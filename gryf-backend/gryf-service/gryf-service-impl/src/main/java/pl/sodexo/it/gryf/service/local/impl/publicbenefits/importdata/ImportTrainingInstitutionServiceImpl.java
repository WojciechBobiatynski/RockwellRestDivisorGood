package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportAddressDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingInstitutionDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;

import java.util.Iterator;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importTrainingInstitutionService")
public class ImportTrainingInstitutionServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    //OVERRIDE

    @Override
    protected String saveData(Row row){
        ImportTrainingInstitutionDTO importDTO = createImportDTO(row);
        TrainingInstitutionDto trainingInstitutionDto = createTrainingInstitutionDTO(importDTO);
        Long trainingInstitutionId = trainingInstitutionService.saveTrainingInstitution(trainingInstitutionDto, true);

        return String.format("Poprawno zapisano dane: instytucje szkoleniową (%s)", trainingInstitutionId);
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
                    ti.setAddressInvoice(new ImportAddressDTO());
                    ti.getAddressInvoice().setAddress(getStringCellValue(cell));
                    break;
                case 4:
                    ti.getAddressInvoice().setZipCode(getStringCellValue(cell));
                    break;
                case 5:
                    ti.getAddressInvoice().setCity(getStringCellValue(cell));
                    break;
                case 6:
                    ti.setAddressCorr(new ImportAddressDTO());
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

    private TrainingInstitutionDto createTrainingInstitutionDTO(ImportTrainingInstitutionDTO importDTO){
        TrainingInstitutionDto dto = new TrainingInstitutionDto();
        dto.setCode(null);
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

        dto.setRemarks(null);
        dto.setContacts(Lists.newArrayList());//TODO: tbilski
        dto.setUsers(Lists.newArrayList());//TODO: tbilski
        return dto;
    }

}
