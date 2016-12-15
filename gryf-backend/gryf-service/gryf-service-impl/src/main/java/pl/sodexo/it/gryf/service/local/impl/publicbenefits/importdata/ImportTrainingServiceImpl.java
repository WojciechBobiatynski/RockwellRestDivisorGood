package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importTrainingService")
public class ImportTrainingServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ImportDataRowRepository importDataRowRepository;

    @Autowired
    private TrainingCategoryRepository trainingCategoryRepository;

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
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportTrainingDTO importDTO = createImportDTO(row);
        validateImport(importDTO);

        TrainingInstitution trainingInstitution = trainingInstitutionRepository.findByExternalId(importDTO.getTrainingInstitutionExternalId());
        Training training = trainingRepository.findByExternalId(importDTO.getExternalId());
        TrainingCategory trainingCategory = trainingCategoryRepository.get(importDTO.getCategory());
        validateConnectedData(importDTO, trainingInstitution, training, trainingCategory);

        TrainingDTO trainingDTO = createTrainingDTO(training, trainingInstitution, trainingCategory, importDTO);
        if(training == null){
            Long trainingId = trainingService.saveTraining(trainingDTO);

            ImportResultDTO result = new ImportResultDTO();
            result.setTrainingId(trainingId);
            result.setDescrption(String.format("Poprawno utworzono dane: szkolenie (%s)", getIdToDescription(trainingId)));
            return result;

        }else{
            trainingService.updateTraining(trainingDTO);

            ImportResultDTO result = new ImportResultDTO();
            result.setTrainingId(trainingDTO.getTrainingId());
            result.setDescrption(String.format("Poprawno zaktualizowano dane: szkolenie (%s)", trainingDTO.getTrainingId()));
            return result;
        }
    }

    @Override
    protected String saveInternalExtraData(ImportParamsDTO paramsDTO, ImportDataRow importDataRow){
        int updateNum = trainingRepository.deactiveTrainings(importDataRow.getImportJob(), GryfUser.getLoggedUserLoginOrDefault());
        return updateNum > 0 ? String.format("Poprawnie deaktywowano szkolenia: ilość zmienionych szkoleń (%s).", updateNum) :
                                "Brak deaktywowanych szkoleń.";
    }

    protected List<Long> getInternalExtraRows(Long importJobId){
        ImportDataRow extraRow = importDataRowRepository.getByImportJobAndRowNum(importJobId, 0);
        return Lists.newArrayList(extraRow.getId());
    }

    //PRIVATE METHODS - VALIDATE & SAVE

    private void validateImport(ImportTrainingDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);

        if(!TrainingCategory.EGZ_TYPE.equals(importDTO.getCategory())) {

            if(importDTO.getHoursNumber() == null){
                violations.add(new EntityConstraintViolation("Ilość godzin lekcyjnych nie może być pusta"));
            }
            if(importDTO.getHourPrice() == null){
                violations.add(new EntityConstraintViolation("Cena 1h szkolenia nie może być pusta"));
            }
            if (importDTO.getHourPrice() != null && importDTO.getHoursNumber() != null && importDTO.getPrice() != null) {
                BigDecimal calcHourPrice = importDTO.getHourPrice().multiply(new BigDecimal(importDTO.getHoursNumber()));
                if (calcHourPrice.compareTo(importDTO.getPrice()) != 0) {
                    violations.add(new EntityConstraintViolation(
                            String.format("Cena szkolenia (%sPLN) nie zgadza się z iloscią godzin (%s) " + "oraz cena za 1h szkolenia (%sPLN). Otrzymany wynik: %sPLN", importDTO.getPrice(), importDTO.getHoursNumber(),
                                    importDTO.getHourPrice(), calcHourPrice)));
                }
            }
        }

        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportTrainingDTO importDTO, TrainingInstitution trainingInstitution,
                                        Training training, TrainingCategory trainingCategory){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstitution == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono instytucji szkoleniowej "
                            + "o identyfikatorze zewnętrzym (%s)", importDTO.getTrainingInstitutionExternalId())));
        }
        if(trainingInstitution != null && training != null){
            if(!training.getTrainingInstitution().equals(trainingInstitution)){
                violations.add(new EntityConstraintViolation(String.format("Szkolenie (%s) jest połączone z instytucją szkoleniową (%s). "
                        + "Nie można zmienić przynależności takiego szkolenia do innej instytucji (%s).",
                        training.getExternalId(), training.getTrainingInstitution().getExternalId(), trainingInstitution.getExternalId())));
            }
        }
        if(trainingCategory == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono kategorii szkolenia dla kodu (%s).",
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
                    t.setReimbursmentCondition(getStringCellValue(cell));
                    break;
            }
        }
        return t;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private TrainingDTO createTrainingDTO(Training training, TrainingInstitution trainingInstitution,
                                        TrainingCategory trainingCategory, ImportTrainingDTO importDTO){
        TrainingDTO dto = new TrainingDTO();
        dto.setTrainingId(training != null ? training.getId() : null);
        dto.setExternalTrainingId(importDTO.getExternalId());
        dto.setTrainingInstitution(trainingInstitution.getId());
        dto.setInstitutionName(null);
        dto.setName(importDTO.getName());
        dto.setPrice(importDTO.getPrice());
        dto.setStartDate(importDTO.getStartDate());
        dto.setEndDate(importDTO.getEndDate());
        dto.setPlace(importDTO.getPlace());
        dto.setHoursNumber(importDTO.getHoursNumber());
        dto.setHourPrice(importDTO.getHourPrice());
        dto.setCategory(trainingCategory.getId());
        dto.setTrainingCategoryCatalogId(null);
        dto.setReimbursmentConditions(importDTO.getReimbursmentCondition());
        dto.setActive(true);
        dto.setDeactivateUser(null);
        dto.setDeactivateDate(null);
        dto.setDeactivateJobId(null);

        dto.setVersion(training != null ? training.getVersion() : null);
        dto.setCreatedUser(training != null ? training.getCreatedUser() : null);
        dto.setCreatedTimestamp(training != null ? training.getCreatedTimestamp() : null);

        return dto;
    }

}
