package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.GenericBuilder;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
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
import pl.sodexo.it.gryf.service.api.patterns.DefaultPatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.service.api.publicbenefits.importdata.ImportTrainingValidator;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceExtService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;
import pl.sodexo.it.gryf.service.local.impl.dictionaries.PriceValidationType;
import pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions.TrainingValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = ImportDataService.IMPORT_TRAINING_SERVICE)
public class ImportTrainingServiceImpl extends ImportBaseDataServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportTrainingServiceImpl.class);

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

    @Autowired
    private List<ImportTrainingValidator> importTrainingValidators;

    @Autowired
    private TrainingValidator trainingValidator;

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
        Training training = externalId != null ? trainingRepository.findByExternalIdAndProgramId(externalId, paramsDTO.getGrantProgramId()) : null;

        ImportResultDTO result = new ImportResultDTO();
        result.setTrainingId(training != null ? training.getId() : null);
        return result;
    }

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row, Long importJobId) {
        ImportTrainingDTO importDTO = createImportDTO(row);
        validateImport(paramsDTO, importDTO);

        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getTrainingInstitutionExternalId());
        Training training = trainingRepository.findByExternalIdAndProgramId(importDTO.getExternalId(), paramsDTO.getGrantProgramId());
        TrainingCategory trainingCategory = trainingCategoryRepository.get(importDTO.getCategory());
        validateConnectedData(importDTO, trainingInstitution, training, trainingCategory);

        ImportResultDTO result = new ImportResultDTO();
        TrainingDTO trainingDTO = createTrainingDTO(training, trainingInstitution, trainingCategory, importDTO, paramsDTO);
        TrainingInstanceExtDTO trainingInstanceExt = createTrainingInstanceExtDTO(training, trainingInstitution, trainingCategory, importDTO);

        if(training == null){
            Long trainingId = trainingService.saveTraining(trainingDTO);
            trainingInstanceExt.setTrainingId(trainingId);
            trainingInstanceExtService.saveTrainingInstanceExt(trainingInstanceExt, importJobId);
            result.setTrainingId(trainingId);
            result.setDescrption(String.format("Poprawnie utworzono dane: us??uga (%s).", getIdToDescription(trainingId)));

        }else{
            trainingService.updateTraining(trainingDTO);
            trainingInstanceExtService.saveTrainingInstanceExt(trainingInstanceExt, importJobId);
            result.setTrainingId(trainingDTO.getTrainingId());
            result.setDescrption(String.format("Poprawnie zaktualizowano dane: us??uga (%s).", trainingDTO.getTrainingId()));
        }

        return result;
    }

    @Override
    protected String saveInternalExtraData(ImportParamsDTO paramsDTO, ImportDataRow importDataRow){
        int updateNum = trainingRepository.deactiveTrainings(paramsDTO.getGrantProgramId(), importDataRow.getImportJob(), GryfUser.getLoggedUserLoginOrDefault());

        int deleteNum = trainingInstanceExtRepository.deleteAllTrainingsInstanceExt(paramsDTO.getGrantProgramId(), importDataRow.getImportJob().getId());

        LOGGER.debug("trainingInstanceExtRepository.deleteAllTrainingsInstanceExt Result = {} ", deleteNum);

        return updateNum > 0 ? String.format("Poprawnie deaktywowano us??ugi: ilo???? zmienionych us??ug (%s).", updateNum) :
                                "Brak deaktywowanych us??ug.";
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
            sb.append(String.format("Wczytano wszystkie wiersze. Ilo???? wczytanych wierszy: %s. ", normalSuccessRows));
        }else {
            sb.append(String.format("Wczytano cz????ciowo wiersze. Ilo???? wszystkich wierszy: %s, ilo???? wierszy poprawnie wczytanych: %s, "
                            + "ilo???? wierszy b????dnych (biznesowe): %s, ilo???? wierszy b??ednych (krytyczne): %s. ",
                    allRows, normalSuccessRows, normalBussinssRows, normalErrorRows));
        }

        if(extraBussinssRows == 0 && extraErrorRows == 0){
            sb.append("Deaktywacja nistniej??cych rekord??w przebieg??a pomy??lnie.");
        }else{
            sb.append("Nie uda??o si?? deaktywowa?? nieistniej??cych rekord??w z powodu b??ed??w ").
                    append(extraBussinssRows != 0 ? "biznesowych" : "krytycznych").append(".");
        }
        return sb.toString();
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportParamsDTO paramsDTO, ImportTrainingDTO importTrainingDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importTrainingDTO);
        if (importTrainingDTO.getIndOrderExternalId() == null) {
            violations.add(new EntityConstraintViolation("Brak identyfikatora wsparcia"));
        } else {
            //Wybor wzorca per program z parametrami
            PatternContext importTrainingPatternContext = GenericBuilder.of(() -> new DefaultPatternContext(paramsDTO.getGrantProgramId(), paramsDTO.getGrantProgram().getProgramCode(), ""))
                    .with(DefaultPatternContext::setId, paramsDTO.getGrantProgramId()).build();
            String searchPattern = importPatternService.getPattern(importTrainingPatternContext);

            Pattern r = Pattern.compile(searchPattern);
            Matcher m = r.matcher(importTrainingDTO.getIndOrderExternalId());
            if (!m.find()) {
                violations.add(new EntityConstraintViolation("B????dny format identyfikatora wsparcia " + importTrainingDTO.getIndOrderExternalId()));
            }
        }

        if(!trainingValidator.isExcludedCategoryFromPriceValidating(importTrainingDTO.getCategory())){

            if(importTrainingDTO.getHoursNumber() == null){
                violations.add(new EntityConstraintViolation("Ilo???? godzin lekcyjnych nie mo??e by?? pusta"));
            }
            if(importTrainingDTO.getHourPrice() == null){
                violations.add(new EntityConstraintViolation("Cena 1h us??ugi nie mo??e by?? pusta"));
            }
            if (importTrainingDTO.getHourPrice() != null && importTrainingDTO.getHoursNumber() != null && importTrainingDTO.getPrice() != null) {
                String importPriceValidateType = importTrainingDTO.getPriceValidateType();
                if (importPriceValidateType != null && PriceValidationType.PRICE_ROUNDED.getCode().equals(importPriceValidateType)) {
                    BigDecimal calcHourPrice = importTrainingDTO.getPrice().divide(BigDecimal.valueOf(importTrainingDTO.getHoursNumber()), 2, RoundingMode.HALF_UP);
                    if (calcHourPrice.compareTo(importTrainingDTO.getHourPrice()) != 0) {
                        violations.add(new EntityConstraintViolation(
                                String.format("Zaimportowana zaokr??glona cena za 1h us??ugi (%sPLN) nie zgadza si?? z wyliczon?? cen??: %sPLN", importTrainingDTO.getHourPrice(), calcHourPrice)));
                    }
                } else {
                    BigDecimal calcHourPrice = importTrainingDTO.getHourPrice().multiply(BigDecimal.valueOf(importTrainingDTO.getHoursNumber()));
                    if (calcHourPrice.compareTo(importTrainingDTO.getPrice()) != 0) {
                        violations.add(new EntityConstraintViolation(
                                String.format("Cena us??ugi (%sPLN) nie zgadza si?? z ilosci?? godzin (%s) " + "oraz cena za 1h us??ugi (%sPLN). Otrzymany wynik: %sPLN", importTrainingDTO.getPrice(), importTrainingDTO.getHoursNumber(),
                                        importTrainingDTO.getHourPrice(), calcHourPrice)));
                    }
                }
            }
        }

        if(Strings.isNullOrEmpty(importTrainingDTO.getCertificateRemark()) && Strings.isNullOrEmpty(importTrainingDTO.getIndOrderExternalId())){
            violations.add(new EntityConstraintViolation("Pola 'Certyfikat - opis' oraz 'Identyfikatora wsparcia' nie moga by?? jednocze??nie puste."));
        }

        //Walidacja importu
        importTrainingValidators.forEach(importTrainingValidator -> violations.addAll(importTrainingValidator.validate(paramsDTO.getGrantProgram(), importTrainingDTO)));

        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportTrainingDTO importDTO, TrainingInstitution trainingInstitution,
                                        Training training, TrainingCategory trainingCategory){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstitution == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono Us??ugodawcy "
                            + "o identyfikatorze zewn??trzym (%s)", importDTO.getTrainingInstitutionExternalId())));
        }
        if(trainingInstitution != null && training != null){
            if(!training.getTrainingInstitution().equals(trainingInstitution)){
                violations.add(new EntityConstraintViolation(String.format("Us??uga (%s) jest po????czone z us??ugodawcy?? szkoleniow?? (%s). "
                        + "Nie mo??na zmieni?? przynale??no??ci takiej us??ugi do innej us??ugodawcy (%s).",
                        training.getExternalId(), training.getTrainingInstitution().getExternalId(), trainingInstitution.getExternalId())));
            }
        }
        if(trainingCategory == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kategorii us??ugi dla kodu (%s).",
                                                        importDTO.getCategory())));
        }
        gryfValidator.validate(violations);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    /**
     * Odczyt rekordu/wiersza z pliku importu us??ug
     * @param row rekord z danymi
     * @return ImportTrainingDTO
     */
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
                    t.setSubcategory(getStringCellValue(cell));
                    break;
                case 13:
                    t.setIsExam(getStringCellValue(cell));
                    break;
                case 14:
                    t.setCertificate(getStringCellValue(cell));
                    break;
                case 15:
                    t.setCertificateRemark(getStringCellValue(cell));
                    break;
                case 16:
                    t.setIndOrderExternalId(getStringCellValue(cell));
                    break;
                case 17:
                    t.setStatus(getStringCellValue(cell));
                    break;
                case 18:
                    t.setIsQualification(getStringCellValue(cell));
                    break;
                case 19:
                    t.setIsOtherQualification(getStringCellValue(cell));
                    break;
                case 20:
                    t.setQualificationCode(getStringCellValue(cell));
                    break;
                case 21:
                    t.setRegistrationDate(getDateCellValue(cell));
                    break;
                case 22:
                    t.setMaxParticipantsCount(getIntegerCellValue(cell));
                    break;
                case 23:
                    t.setPriceValidateType(getStringCellValue(cell));
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
                                          TrainingCategory trainingCategory, ImportTrainingDTO importDTO, ImportParamsDTO paramsDTO){
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
        dto.setReimbursmentConditions(concateReimbursmentConditions(importDTO.getCertificateRemark(), importDTO.getIndOrderExternalId()));
        dto.setMaxParticipantsCount(importDTO.getMaxParticipantsCount());

        dto.setActive(true);
        dto.setDeactivateUser(null);
        dto.setDeactivateDate(null);
        dto.setDeactivateJobId(null);

        dto.setVersion(training != null ? training.getVersion() : null);
        dto.setCreatedUser(training != null ? training.getCreatedUser() : null);
        dto.setCreatedTimestamp(training != null ? training.getCreatedTimestamp() : null);
        dto.setModifiedUser(training != null ? training.getModifiedUser() : null);
        dto.setModifiedTimestamp(training != null ? training.getModifiedTimestamp() : null);

        dto.setGrantProgramId(paramsDTO.getGrantProgramId());

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
        dto.setCertificateRemark(importDTO.getCertificateRemark());
        dto.setIndOrderExternalId(importDTO.getIndOrderExternalId());
        dto.setSubcategory(importDTO.getSubcategory());
        dto.setIsExam(importDTO.getIsExam());
        dto.setCertificate(importDTO.getCertificate());
        dto.setStatus(importDTO.getStatus());
        dto.setIsQualification(importDTO.getIsQualification());
        dto.setIsOtherQualification(importDTO.getIsOtherQualification());
        dto.setQualificationCode(importDTO.getQualificationCode());
        dto.setRegistrationDate(importDTO.getRegistrationDate());
        dto.setMaxParticipantsCount(importDTO.getMaxParticipantsCount());
        dto.setPriceValidateType(importDTO.getPriceValidateType());
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

