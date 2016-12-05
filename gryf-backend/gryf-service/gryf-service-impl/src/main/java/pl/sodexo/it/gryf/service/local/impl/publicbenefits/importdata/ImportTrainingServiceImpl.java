package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;

import java.math.BigDecimal;
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
    protected String saveData(Row row){
        ImportTrainingDTO importDTO = createImportDTO(row);
        TrainingDTO trainingDTO = createTrainingDTO(importDTO);
        Long trainingId = trainingService.saveTraining(trainingDTO);

        return String.format("Poprawno zapisano dane: szkolenie (%s)", trainingId);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportTrainingDTO createImportDTO(Row row){
        ImportTrainingDTO t = new ImportTrainingDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    t.setVatRegNum(cell.getStringCellValue());
                    break;
                case 1:
                    t.setTrainingInstanceName(cell.getStringCellValue());
                    break;
                case 2:
                    t.setExternalId((long)cell.getNumericCellValue());
                    break;
                case 3:
                    t.setName(cell.getStringCellValue());
                    break;
                case 4:
                    t.setStartDate(cell.getDateCellValue());
                    break;
                case 5:
                    t.setEndDate(cell.getDateCellValue());
                    break;
                case 6:
                    t.setStatus(cell.getStringCellValue());
                    break;
                case 7:
                    t.setPlace(cell.getStringCellValue());
                    break;
                case 8:
                    t.setPrice(new BigDecimal(cell.getNumericCellValue()));
                    break;
                case 9:
                    t.setHoursNumber((int)cell.getNumericCellValue());
                    break;
                case 10:
                    t.setHourPrice(new BigDecimal(cell.getNumericCellValue()));
                    break;
                case 11:
                    t.setCategory(cell.getStringCellValue());
                    break;
                case 12:
                    t.setReimbursmentCondition(cell.getStringCellValue());
                    break;
            }
        }
        return t;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingDTO createTrainingDTO(ImportTrainingDTO importDTO){
        TrainingDTO dto = new TrainingDTO();



        return dto;
    }

}
