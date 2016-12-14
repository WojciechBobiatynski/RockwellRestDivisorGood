package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportAddressCorrDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportAddressInvoiceDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowError;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
public abstract class ImportBaseDataServiceImpl implements ImportDataService{

    //PRIVATE FILEDS

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Autowired
    private ImportDataRowRepository importDataRowRepository;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    //PUBLIC METHODS

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int saveEmptyRows(Long importJobId, int rowNums){
        int savedSize = 0;
        savedSize += saveEmptyExtraRows(importJobId, rowNums);
        savedSize += saveEmptyNormalRows(importJobId, rowNums);
        return savedSize;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveData(Long importJobId, ImportParamsDTO paramsDTO, Row row){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());

        ImportResultDTO result = saveInternalNormalData(paramsDTO, row);
        rowInfo.setDescription(GryfStringUtils.substring(result.getDescrption(), 0, ImportDataRow.DESCRIPTION_MAX_SIZE));
        rowInfo.setStatus(ImportDataRowStatus.S);

        rowInfo.setContract(result.getContractId() != null ? contractRepository.get(result.getContractId()) : null);
        rowInfo.setIndividual(result.getIndividualId() != null ? individualRepository.get(result.getIndividualId()) : null);
        rowInfo.setEnterprise(result.getEnterpriseId() != null ? enterpriseRepository.get(result.getEnterpriseId()) : null);
        rowInfo.setOrder(result.getOrderId() != null ? orderRepository.get(result.getOrderId()) : null);
        rowInfo.setTrainingInstitution(result.getTrainingInstitutionId() != null ? trainingInstitutionRepository.get(result.getTrainingInstitutionId()) : null);
        rowInfo.setTraining(result.getTrainingId() != null ? trainingRepository.get(result.getTrainingId()) : null);

        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    //PUBLIC METGHODS - EXTRA DATA

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Long> getExtraRows(Long importJobId){
        return getInternalExtraRows(importJobId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveExtraRow(Long extraRowId, ImportParamsDTO paramsDTO){
        ImportDataRow rowInfo = importDataRowRepository.get(extraRowId);

        String description = saveInternalExtraData(paramsDTO, rowInfo);

        rowInfo.setDescription(GryfStringUtils.substring(description, 0, ImportDataRow.DESCRIPTION_MAX_SIZE));
        rowInfo.setStatus(ImportDataRowStatus.S);
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    //PUBLIC METHODS - ERROR METHODS

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveEntityValidationError(Long importJobId, Row row, EntityValidationException e){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());
        saveEntityValidationError(rowInfo, e);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveEntityValidationError(Long rowId, EntityValidationException e){
        ImportDataRow rowInfo = importDataRowRepository.get(rowId);
        saveEntityValidationError(rowInfo, e);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRuntimeError(Long importJobId, Row row, RuntimeException e){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());
        saveRuntimeError(rowInfo, e);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRuntimeError(Long rowId, RuntimeException e){
        ImportDataRow rowInfo = importDataRowRepository.get(rowId);
        saveRuntimeError(rowInfo, e);
    }

    //PROTECTED METHODS

    protected int saveEmptyNormalRows(Long importJobId, int rowNums){
        AsynchronizeJob importJob = asynchronizeJobRepository.get(importJobId);
        for(int i = 0; i< rowNums; i++){
            ImportDataRow row = new ImportDataRow();
            row.setImportJob(importJob);
            row.setRowNum(i + 1);
            row.setDescription(null);
            row.setStatus(ImportDataRowStatus.N);
            importDataRowRepository.save(row);
        }
        return rowNums;
    }

    protected int saveEmptyExtraRows(Long importJobId, int rowNums){
        return 0;
    }

    protected abstract ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row);

    protected String saveInternalExtraData(ImportParamsDTO paramsDTO, ImportDataRow importDataRow){
        return null;
    }

    protected List<Long> getInternalExtraRows(Long importJobId){
        return Lists.newArrayList();
    }

    //PROTECTED METHODS - SAVE EXCEPTIONS

    protected void saveEntityValidationError(ImportDataRow rowInfo, EntityValidationException e) {
        rowInfo.setDescription("Wystapiły błędy biznesowe");
        rowInfo.setStatus(ImportDataRowStatus.E);
        for (EntityConstraintViolation v : e.getViolations()) {
            ImportDataRowError rowError = new ImportDataRowError();
            rowError.setPath(v.getPath());
            rowError.setMessage(v.getMessage());
            rowInfo.addError(rowError);
        }
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    protected void saveRuntimeError(ImportDataRow rowInfo, RuntimeException e){
        rowInfo.setDescription(GryfStringUtils.substring("Wystapił nieoczekowany krytyczny błąd: " + e.getMessage(),
                                                0, ImportDataRow.DESCRIPTION_MAX_SIZE));
        rowInfo.setStatus(ImportDataRowStatus.F);
        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    //PROTECTED METHODS

    protected ZipCodeDto createZipCodeDTO(ImportAddressInvoiceDTO address){
        ZipCode zipCode = zipCodeRepository.findActiveByCode(address.getZipCode());
        if(zipCode != null){
            ZipCodeDto dto = new ZipCodeDto();
            dto.setId(zipCode.getId());
            return dto;
        }
        return null;
    }

    protected ZipCodeDto createZipCodeDTO(ZipCode zipCode){
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
