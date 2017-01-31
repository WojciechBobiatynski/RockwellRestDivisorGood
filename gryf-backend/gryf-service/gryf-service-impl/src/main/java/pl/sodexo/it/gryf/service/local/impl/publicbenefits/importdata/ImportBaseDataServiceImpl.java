package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowError;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
public abstract class ImportBaseDataServiceImpl implements ImportDataService{

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    //PRIVATE FILEDS

    @Autowired
    private ImportDataRowRepository importDataRowRepository;

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

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

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
    public void saveImportDataRowBeforeSaveData(Long importJobId, ImportParamsDTO paramsDTO, Row row){
        ImportDataRow rowInfo = importDataRowRepository.getByImportJobAndRowNum(importJobId, row.getRowNum());

        ImportResultDTO result = saveInternalImportDataRowBeforeSaveData(paramsDTO, row);

        rowInfo.setContract(result.getContractId() != null ? contractRepository.get(result.getContractId()) : null);
        rowInfo.setIndividual(result.getIndividualId() != null ? individualRepository.get(result.getIndividualId()) : null);
        rowInfo.setEnterprise(result.getEnterpriseId() != null ? enterpriseRepository.get(result.getEnterpriseId()) : null);
        rowInfo.setOrder(result.getOrderId() != null ? orderRepository.get(result.getOrderId()) : null);
        rowInfo.setTrainingInstitution(result.getTrainingInstitutionId() != null ? trainingInstitutionRepository.get(result.getTrainingInstitutionId()) : null);
        rowInfo.setTraining(result.getTrainingId() != null ? trainingRepository.get(result.getTrainingId()) : null);
        rowInfo.setTrainingInstance(result.getTrainingInstanceId() != null ? trainingInstanceRepository.get(result.getTrainingInstanceId()) : null);

        importDataRowRepository.update(rowInfo, rowInfo.getId());
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
        rowInfo.setTrainingInstance(result.getTrainingInstanceId() != null ? trainingInstanceRepository.get(result.getTrainingInstanceId()) : null);

        importDataRowRepository.update(rowInfo, rowInfo.getId());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String createDescription(int allRows, int normalSuccessRows, int normalBussinssRows, int normalErrorRows,
                                                int extraSuccessRows, int extraBussinssRows, int extraErrorRows){
        return createInternalDescription(allRows, normalSuccessRows, normalBussinssRows, normalErrorRows,
                                                extraSuccessRows, extraBussinssRows, extraErrorRows);
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
        return importDataRowRepository.saveRowsForJob(importJobId, ImportDataRowStatus.N, GryfUser.getLoggedUserLoginOrDefault(), rowNums);
    }

    protected int saveEmptyExtraRows(Long importJobId, int rowNums){
        return 0;
    }

    protected abstract ImportResultDTO saveInternalImportDataRowBeforeSaveData(ImportParamsDTO paramsDTO, Row row);

    protected abstract ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row);

    protected String saveInternalExtraData(ImportParamsDTO paramsDTO, ImportDataRow importDataRow){
        return null;
    }

    protected List<Long> getInternalExtraRows(Long importJobId){
        return Lists.newArrayList();
    }

    protected String createInternalDescription(int allRows, int normalSuccessRows, int normalBussinssRows, int normalErrorRows,
                                                                int extraSuccessRows, int extraBussinssRows, int extraErrorRows){
        if(allRows == normalSuccessRows){
            return String.format("Wczytano wszystkie wiersze. Ilość wczytanych wierszy: %s.", normalSuccessRows);
        }
        return String.format("Wczytano częściowo wiersze. Ilość wszystkich wierszy: %s, ilość wierszy poprawnie wczytanych: %s, "
                + "ilość wierszy błędnych (biznesowe): %s, ilość wierszy błednych (krytyczne): %s.", allRows, normalSuccessRows, normalBussinssRows, normalErrorRows);
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
            if(isEmpty(cell)) {
                return null;
            }
            if(isNumber(cell)) {
                double valueNumberic = cell.getNumericCellValue();
                return (valueNumberic == (long) valueNumberic) ? String.format("%d", (long) valueNumberic) :
                                                                 String.format("%s",valueNumberic);
            }
            return cell.getStringCellValue().trim();

        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected List<String> getStringListCellValue(Cell cell){
        try{
            if(isEmpty(cell)) {
                return null;
            }
            if(isNumber(cell)) {
                throw new RuntimeException(String.format("Nie można odcyzta listy z komórki która jest typu numerycznego."));
            }
            List<String> result = new ArrayList<>();
            String[] valueTab = cell.getStringCellValue().split("[,;:]");
            for(int i = 0; i < valueTab.length; i++){
                String value = valueTab[i].trim();
                if(!Strings.isNullOrEmpty(value)){
                    result.add(valueTab[i].trim());
                }

            }
            return result;

        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Integer getIntegerCellValue(Cell cell){
        try{
            if(isEmpty(cell)) {
                return null;
            }
            if(isString(cell)) {
                String valueStr = cell.getStringCellValue().trim();
                return Integer.valueOf(valueStr);
            }
            double valueNumberic = cell.getNumericCellValue();
            if(valueNumberic != (int) valueNumberic){
                throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                        + "z kolumny numer %s. Komunikat: Wartość %s musi być liczbą całkowitą.", cell.getColumnIndex() + 1, valueNumberic));
            }
            return (int)valueNumberic;

        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected BigDecimal getBigDecimalCellValue(Cell cell){
        try{
            if(isEmpty(cell)) {
                return null;
            }
            if(isString(cell)) {
                String valueStr = cell.getStringCellValue().trim();
                return new BigDecimal(valueStr);
            }
            return BigDecimal.valueOf(cell.getNumericCellValue());

        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Long getLongCellValue(Cell cell){
        boolean retrowThisException = false;
        try{
            if(isEmpty(cell)) {
                return null;
            }
            if(isString(cell)) {
                String valueStr = cell.getStringCellValue().trim();
                return Long.valueOf(valueStr);
            }
            double valueNumberic = cell.getNumericCellValue();
            if(valueNumberic != (long) valueNumberic){
                retrowThisException = true;
                throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                        + "z kolumny numer %s. Komunikat: Wartość %s musi być liczbą całkowitą.", cell.getColumnIndex() + 1, valueNumberic));
            }
            return (long)valueNumberic;

        }catch(RuntimeException e){
            if(retrowThisException){
                throw e;
            }
            throw new RuntimeException(String.format("Błąd przy pobraniu wartości "
                    + "z kolumny numer %s. Komunikat: %s.", cell.getColumnIndex() + 1, e.getMessage()), e);
        }
    }

    protected Date getDateCellValue(Cell cell){
        try{
            if(isEmpty(cell)) {
                return null;
            }
            if(isString(cell)) {
                String valueStr = cell.getStringCellValue().trim();
                return new SimpleDateFormat(FORMAT_DATE).parse(valueStr);
            }
            return cell.getDateCellValue();

        }catch(RuntimeException | ParseException e){
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

    protected boolean isString(Cell cell){
        //Jezeli przejdziemy na POI 3.15 to użyć: CellType.BLANK == cell.getCellType()
        return CellType.STRING.getCode() == cell.getCellType();
    }

    protected boolean isNumber(Cell cell){
        //Jezeli przejdziemy na POI 3.15 to użyć: CellType.BLANK == cell.getCellType()
        return CellType.NUMERIC.getCode() == cell.getCellType();
    }

}
