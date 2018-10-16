package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata.wz;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.service.api.patterns.DefaultPatternContext;
import pl.sodexo.it.gryf.common.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.common.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceExtRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceExtService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata.ImportBaseDataServiceImpl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = ImportDataService.WZ_IMPORT_TRAINING_SERVICE)
public class ImportTrainingWZServiceImpl extends ImportBaseDataServiceImpl {
    //PRIVATE FIELDS

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingInstanceExtService trainingInstanceExtService;


    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingInstanceExtRepository trainingInstanceExtRepository;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ImportDataRowRepository importDataRowRepository;

    @Autowired
    private TrainingCategoryRepository trainingCategoryRepository;

    @Autowired
    @Qualifier (PatternService.IMPORT_TRAINING_PATTERN_SERVICE)
    private PatternService importPatternService;

    //OVERRIDE

    @Override
    protected int saveEmptyExtraRows(Long importJobId, int rowNums) {
        ImportDataRow rowExtra = new ImportDataRow();
        rowExtra.setImportJob(asynchronizeJobRepository.get(importJobId));
        rowExtra.setRowNum(0);
        rowExtra.setDescription(null);
        rowExtra.setStatus(ImportDataRowStatus.N);
        importDataRowRepository.save(rowExtra);
        return 1;
    }

    @Override
    protected ImportResultDTO saveInternalImportDataRowBeforeSaveData(ImportParamsDTO paramsDTO, Row row){
        String externalId = getExternalId(row);
        Training training = externalId != null ? trainingRepository.findByExternalId(externalId) : null;

        ImportResultDTO result = new ImportResultDTO();
        result.setTrainingId(training != null ? training.getId() : null);
        return result;
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row, Long importJobId) {
        ImportTrainingDTO importDTO = createImportDTO(row);
        validateImport(importDTO);

        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getTrainingInstitutionExternalId());
        Training training = trainingRepository.findByExternalId(importDTO.getExternalId());
        TrainingCategory trainingCategory = trainingCategoryRepository.get(importDTO.getCategory());
        validateConnectedData(importDTO, trainingInstitution, training, trainingCategory);

        ImportResultDTO result = new ImportResultDTO();
        TrainingDTO trainingDTO = createTrainingDTO(training, trainingInstitution, trainingCategory, importDTO);
        TrainingInstanceExtDTO trainingInstanceExt = createTrainingInstanceExtDTO(training, trainingInstitution, trainingCategory, importDTO);

        if(training == null){
            Long trainingId = trainingService.saveTraining(trainingDTO);
            Long trainingInstanceExtId = trainingInstanceExtService.saveTrainingInstanceExt(trainingInstanceExt, importJobId);
            result.setTrainingId(trainingId);
            result.setDescrption(String.format("Poprawnie utworzono dane: usługa (%s).", getIdToDescription(trainingId)));

        }else{
            trainingService.updateTraining(trainingDTO);
            Long trainingInstanceExtId = trainingInstanceExtService.saveTrainingInstanceExt(trainingInstanceExt, importJobId);
            result.setTrainingId(trainingDTO.getTrainingId());
            result.setDescrption(String.format("Poprawnie zaktualizowano dane: usługa (%s).", trainingDTO.getTrainingId()));
        }

        return result;
    }

    @Override
    protected String saveInternalExtraData(ImportParamsDTO paramsDTO, ImportDataRow importDataRow){
        int updateNum = trainingRepository.deactiveTrainings(importDataRow.getImportJob(), GryfUser.getLoggedUserLoginOrDefault());

        int deleteNum = trainingInstanceExtRepository.deleteAllTrainingsInstanceExt(importDataRow.getImportJob().getId());

        return updateNum > 0 ? String.format("Poprawnie deaktywowano usługi: ilość zmienionych usług (%s).", updateNum) :
                                "Brak deaktywowanych usług.";
    }

    @Override
    protected List<Long> getInternalExtraRows(Long importJobId){
        ImportDataRow extraRow = importDataRowRepository.getByImportJobAndRowNum(importJobId, 0);
        return Lists.newArrayList(extraRow.getId());
    }

    @Override
    protected String createInternalDescription(int allRows, int normalSuccessRows, int normalBussinssRows, int normalErrorRows,
            int extraSuccessRows, int extraBussinssRows, int extraErrorRows){
        StringBuilder sb = new StringBuilder();
        if(allRows == normalSuccessRows + (extraSuccessRows + extraBussinssRows + extraErrorRows)){
            sb.append(String.format("Wczytano wszystkie wiersze. Ilość wczytanych wierszy: %s. ", normalSuccessRows));
        }else {
            sb.append(String.format("Wczytano częściowo wiersze. Ilość wszystkich wierszy: %s, ilość wierszy poprawnie wczytanych: %s, "
                            + "ilość wierszy błędnych (biznesowe): %s, ilość wierszy błednych (krytyczne): %s. ",
                    allRows, normalSuccessRows, normalBussinssRows, normalErrorRows));
        }

        if(extraBussinssRows == 0 && extraErrorRows == 0){
            sb.append("Deaktywacja nistniejących rekordów przebiegła pomyślnie.");
        }else{
            sb.append("Nie udało się deaktywować nieistniejących rekordów z powodu błedów ").
                    append(extraBussinssRows != 0 ? "biznesowych" : "krytycznych").append(".");
        }
        return sb.toString();
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportTrainingDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);
        if (importDTO.getReimbursmentCondition2() == null) {
            violations.add(new EntityConstraintViolation("Brak identyfikatora wsparcia"));
        } else {
            PatternContext importTrainingPatternContext = DefaultPatternContext.create().build();
            String searchPattern = importPatternService.getPattern(importTrainingPatternContext);

            Pattern r = Pattern.compile(searchPattern);
            Matcher m = r.matcher(importDTO.getReimbursmentCondition2());
            if (!m.find()) {
                violations.add(new EntityConstraintViolation("Błędny format identyfikatora wsparcia " + importDTO.getReimbursmentCondition2()));
            }
        }

        if(!TrainingCategory.EGZ_TYPE.equals(importDTO.getCategory())) {

            if(importDTO.getHoursNumber() == null){
                violations.add(new EntityConstraintViolation("Ilość godzin lekcyjnych nie może być pusta"));
            }
            if(importDTO.getHourPrice() == null){
                violations.add(new EntityConstraintViolation("Cena 1h usługi nie może być pusta"));
            }
            if (importDTO.getHourPrice() != null && importDTO.getHoursNumber() != null && importDTO.getPrice() != null) {
                BigDecimal calcHourPrice = importDTO.getHourPrice().multiply(BigDecimal.valueOf(importDTO.getHoursNumber()));
                if (calcHourPrice.compareTo(importDTO.getPrice()) != 0) {
                    violations.add(new EntityConstraintViolation(
                            String.format("Cena usługi (%sPLN) nie zgadza się z iloscią godzin (%s) " + "oraz cena za 1h usługi (%sPLN). Otrzymany wynik: %sPLN", importDTO.getPrice(), importDTO.getHoursNumber(),
                                    importDTO.getHourPrice(), calcHourPrice)));
                }
            }
        }

        if(Strings.isNullOrEmpty(importDTO.getReimbursmentCondition1()) && Strings.isNullOrEmpty(importDTO.getReimbursmentCondition2())){
            violations.add(new EntityConstraintViolation("Pola 'Warunek rozliczrenia 1' oraz 'Warunek rozliczenia 2' nie moga być jednocześnie puste."));
        }

        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportTrainingDTO importDTO, TrainingInstitution trainingInstitution,
                                        Training training, TrainingCategory trainingCategory){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstitution == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono Usługodawcy "
                            + "o identyfikatorze zewnętrzym (%s)", importDTO.getTrainingInstitutionExternalId())));
        }
        if(trainingInstitution != null && training != null){
            if(!training.getTrainingInstitution().equals(trainingInstitution)){
                violations.add(new EntityConstraintViolation(String.format("Usługa (%s) jest połączone z usługodawcyą szkoleniową (%s). "
                        + "Nie można zmienić przynależności takiego usługi do innej usługodawcy (%s).",
                        training.getExternalId(), training.getTrainingInstitution().getExternalId(), trainingInstitution.getExternalId())));
            }
        }
        if(trainingCategory == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kategorii usługi dla kodu (%s).",
                                                        importDTO.getCategory())));
        }
        gryfValidator.validate(violations);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportTrainingDTO createImportDTO(Row row){
        ImportTrainingDTO t = new ImportTrainingDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    t.setTrainingInstitutionExternalId(getStringCellValue(cell));
                    break;
                case 1:
                    t.setVatRegNum(getStringCellValue(cell));
                    break;
                case 2:
                    t.setTrainingInstanceName(getStringCellValue(cell));
                    break;
                case 3:
                    t.setExternalId(getStringCellValue(cell));
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
                    t.setPlace(getStringCellValue(cell));
                    break;
                case 8:
                    t.setPrice(getBigDecimalCellValue(cell));
                    break;
                case 9:
                    t.setHoursNumber(getIntegerCellValue(cell));
                    break;
                case 10:
                    t.setHourPrice(getBigDecimalCellValue(cell));
                    break;
                case 11:
                    t.setCategory(getStringCellValue(cell));
                    break;
                case 12:
                    t.setReimbursmentCondition1(getStringCellValue(cell));
                    break;
                case 13:
                    t.setReimbursmentCondition2(getStringCellValue(cell));
                    break;
            }
        }
        return t;
    }

    private String getExternalId(Row row){
        ImportTrainingDTO t = new ImportTrainingDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 3:
                    return getStringCellValue(cell);
            }
        }
        return null;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingDTO createTrainingDTO(Training training, TrainingInstitution trainingInstitution,
                                        TrainingCategory trainingCategory, ImportTrainingDTO importDTO){
        TrainingDTO dto = new TrainingDTO();
        dto.setTrainingId(training != null ? training.getId() : null);
        dto.setExternalTrainingId(importDTO.getExternalId());
        dto.setTrainingInstitution(trainingInstitution.getId());
        dto.setInstitutionName(trainingInstitution.getName());
        dto.setName(importDTO.getName());
        dto.setPrice(importDTO.getPrice());
        dto.setStartDate(importDTO.getStartDate());
        dto.setEndDate(importDTO.getEndDate());
        dto.setPlace(importDTO.getPlace());
        dto.setHoursNumber(importDTO.getHoursNumber());
        dto.setHourPrice(importDTO.getHourPrice());
        dto.setCategory(trainingCategory.getId());
        dto.setTrainingCategoryCatalogId(trainingCategory.getTrainingCategoryCatalog().getId());
        dto.setReimbursmentConditions(concateReimbursmentConditions(importDTO.getReimbursmentCondition1(), importDTO.getReimbursmentCondition2()));
        dto.setActive(true);
        dto.setDeactivateUser(null);
        dto.setDeactivateDate(null);
        dto.setDeactivateJobId(null);

        dto.setVersion(training != null ? training.getVersion() : null);
        dto.setCreatedUser(training != null ? training.getCreatedUser() : null);
        dto.setCreatedTimestamp(training != null ? training.getCreatedTimestamp() : null);
        dto.setModifiedUser(training != null ? training.getModifiedUser() : null);
        dto.setModifiedTimestamp(training != null ? training.getModifiedTimestamp() : null);
        return dto;
    }

    private TrainingInstanceExtDTO createTrainingInstanceExtDTO(Training training, TrainingInstitution trainingInstitution,
                                                                TrainingCategory trainingCategory, ImportTrainingDTO importDTO) {
        TrainingInstanceExtDTO dto = new TrainingInstanceExtDTO();
        dto.setTrainingInstitution(trainingInstitution.getId());
        dto.setVatRegNum(importDTO.getVatRegNum());
        dto.setName(importDTO.getName());
        dto.setTrainingId(training != null ? training.getId() : null);
        dto.setTrainingExternalId(importDTO.getExternalId());
        dto.setTrainingName(importDTO.getTrainingInstanceName());
        dto.setStartDate(importDTO.getStartDate());
        dto.setEndDate(importDTO.getEndDate());
        dto.setPlace(importDTO.getPlace());
        dto.setPrice(importDTO.getPrice());
        dto.setHoursNumber(importDTO.getHoursNumber());
        dto.setHourPrice(importDTO.getHourPrice());
        dto.setCategory(trainingCategory.getId());
        dto.setCertificateRemark(importDTO.getReimbursmentCondition1());
        dto.setIndOrderExternalId(importDTO.getReimbursmentCondition2());
        return dto;
    }
    private String concateReimbursmentConditions(String s1, String s2){
        boolean flagS1 = !Strings.isNullOrEmpty(s1);
        boolean flagS2 = !Strings.isNullOrEmpty(s2);
        StringBuilder sb = new StringBuilder();
        if(flagS1){
            sb.append(s1);
        }
        if(flagS1 && flagS2){
            sb.append(" ");
        }
        if(flagS2){
            sb.append(s2);
        }
        return sb.toString();
    }

}
