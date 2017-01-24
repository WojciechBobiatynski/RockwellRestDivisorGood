package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportOpinionDoneDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2017-01-03.
 */
@Service(value = "importOpinionDoneService")
public class ImportOpinionDoneServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    //OVERRIDE

    @Override
    protected ImportResultDTO saveInternalImportDataRowBeforeSaveData(ImportParamsDTO paramsDTO, Row row){
        return new ImportResultDTO();
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportOpinionDoneDTO importDTO = createImportDTO(row);
        validateImport(paramsDTO, importDTO);

        Long trainingInstanceId = trainingInstanceService.updateOpinionDone(importDTO.getTrainingExternalId(),
                                                    importDTO.getPesel(), getBoolean(importDTO.getOpinionDone()));

        ImportResultDTO result = new ImportResultDTO();
        result.setTrainingInstanceId(trainingInstanceId);
        result.setDescrption(String.format("Poprawnie zaktualizowano dane: rezerwacja szkolenia (%s).", getIdToDescription(trainingInstanceId)));
        return result;
    }

    //PRIVATE METHODS - VALIDATE

    private void validateImport(ImportParamsDTO paramsDTO, ImportOpinionDoneDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);
        gryfValidator.validate(violations);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportOpinionDoneDTO createImportDTO(Row row){
        ImportOpinionDoneDTO o = new ImportOpinionDoneDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    o.setTrainingExternalId(getStringCellValue(cell));
                    break;
                case 1:
                    o.setPesel(getStringCellValue(cell));
                    break;
                case 2:
                    o.setOpinionDone(getStringCellValue(cell));
                    break;
            }
        }
        return o;
    }

    private boolean getBoolean(String value){
        return "T".equals(value);
    }
}
