package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importTrainingService")
public class ImportTrainingServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private TrainingService trainingService;

    //OVERRIDE

    @Override
    protected String saveData(ImportParamsDTO paramsDTO, Row row){
        ImportTrainingDTO importDTO = createImportDTO(row);
        TrainingDTO trainingDTO = createTrainingDTO(importDTO);
        Long trainingId = trainingService.saveTraining(trainingDTO);

        return String.format("Poprawno zapisano dane: szkolenie (%s)", getIdToDescription(trainingId));
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportTrainingDTO createImportDTO(Row row){
        ImportTrainingDTO t = new ImportTrainingDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    t.setTrainingInstanceExternalId(getLongCellValue(cell));
                    break;
                case 1:
                    t.setVatRegNum(getStringCellValue(cell));
                    break;
                case 2:
                    t.setTrainingInstanceName(getStringCellValue(cell));
                    break;
                case 3:
                    t.setExternalId(getLongCellValue(cell));
                    break;
                case 4:
                    t.setName(getStringCellValue(cell));
                    break;
                case 5:
                    t.setStartDate(getDateCellValue(cell));
                    break;
                case 6:
                    t.setEndDate(getDateCellValue(cell));
                    break;
                case 7:
                    t.setStatus(getStringCellValue(cell));
                    break;
                case 8:
                    t.setPlace(getStringCellValue(cell));
                    break;
                case 9:
                    t.setPrice(getBigDecimalCellValue(cell));
                    break;
                case 10:
                    t.setHoursNumber(getIntegerCellValue(cell));
                    break;
                case 11:
                    t.setHourPrice(getBigDecimalCellValue(cell));
                    break;
                case 12:
                    t.setCategory(getStringCellValue(cell));
                    break;
                case 13:
                    t.setReimbursmentCondition(getStringCellValue(cell));
                    break;
            }
        }
        return t;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingDTO createTrainingDTO(ImportTrainingDTO importDTO){
        TrainingDTO dto = new TrainingDTO();
        dto.setTrainingId(null);
        dto.setTrainingInstitution(1l);//TODO: tbilski
        dto.setInstitutionName(null);
        dto.setName(importDTO.getName());
        dto.setPrice(importDTO.getPrice());
        dto.setStartDate(importDTO.getStartDate());
        dto.setEndDate(importDTO.getEndDate());
        dto.setPlace(importDTO.getPlace());
        dto.setHoursNumber(importDTO.getHoursNumber());
        dto.setHourPrice(importDTO.getHourPrice());
        dto.setCategory(importDTO.getCategory());
        dto.setTrainingCategoryCatalogId(null);
        return dto;
    }

}
