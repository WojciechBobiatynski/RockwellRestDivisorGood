package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportAddressDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowError;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-11-30.
 */
public abstract class ImportBaseDataServiceImpl implements ImportDataService{

    //PRIVATE FILEDS

    @Autowired
    private ImportDataRowRepository importDataRowRepository;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    //PUBLIC METHODS

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEmptyRows(Long importJobId, int rowNums){
        for(int i = 0; i< rowNums; i++){
            ImportDataRow row = new ImportDataRow();
            row.setImportJob(importJobId);
            row.setRowNum(i + 1);
            row.setDescription(null);
            row.setStatus(ImportDataRowStatus.N);
            importDataRowRepository.save(row);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveData(Long importJobId, ImportParamsDTO paramsDTO, Row row){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());

        String description = saveData(paramsDTO, row);

        rowInfo.setDescription(description);
        rowInfo.setStatus(ImportDataRowStatus.S);
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    protected abstract String saveData(ImportParamsDTO paramsDTO, Row row);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEntityValidationError(Long importJobId, Row row, EntityValidationException e){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());
        rowInfo.setDescription("Wystapiły błędy biznesowe");
        rowInfo.setStatus(ImportDataRowStatus.E);
        for(EntityConstraintViolation v : e.getViolations()){
            ImportDataRowError rowError = new ImportDataRowError();
            rowError.setPath(v.getPath());
            rowError.setMessage(v.getMessage());
            rowInfo.addError(rowError);
        }
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRuntimeError(Long importJobId, Row row, RuntimeException e){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());
        rowInfo.setDescription("Wystapił nieoczekowany krytyczny błąd: " + e.getMessage());
        rowInfo.setStatus(ImportDataRowStatus.F);
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    //PROTECTED METHODS

    protected ZipCodeDto createZipCodeDTO(ImportAddressDTO address){
        ZipCode zipCode = zipCodeRepository.findActiveByCode(address.getZipCode());
        if(zipCode != null){
            ZipCodeDto dto = new ZipCodeDto();
            dto.setId(zipCode.getId());
            return dto;
        }
        return null;
    }

    protected String getStringCellValue(Cell cell){
        try{
            String val = isEmpty(cell) ? null : cell.getStringCellValue();
            return (val != null) ? val.trim() : null;
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Integer getIntegerCellValue(Cell cell){
        try{
            return isEmpty(cell) ? null : (int) cell.getNumericCellValue();
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected BigDecimal getBigDecimalCellValue(Cell cell){
        try{
            return isEmpty(cell) ? null : new BigDecimal(cell.getNumericCellValue());
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Long getLongCellValue(Cell cell){
        try{
            return isEmpty(cell) ? null : (long) cell.getNumericCellValue();
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Double getDoubleCellValue(Cell cell){
        try{
            return isEmpty(cell) ? null : cell.getNumericCellValue();
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Date getDateCellValue(Cell cell){
        try{
            return isEmpty(cell) ? null : cell.getDateCellValue();
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Object getIdToDescription(Object val){
        return val == null ? "brak" : val;
    }

    protected boolean isEmpty(Cell cell){
        //Jezeli przejdziemy na POI 3.15 to użyć: CellType.BLANK == cell.getCellType()
        return CellType.BLANK.getCode() == cell.getCellType();
    }

}
