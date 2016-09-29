package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.FileType;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.ReportTemplateCode;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.YesNo;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementComplete;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementCorrect;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantOwnerAidProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.model.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementService;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Service
@Transactional
public class ReimbursementServiceImpl implements ReimbursementService {

    //STATIC FIELDS

    private static final Map<String, Set<String>> statusMap;

    //FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    @Autowired
    private ReimbursementTrainingRepository reimbursementTrainingRepository;

    @Autowired
    private GrantOwnerAidProductRepository grantOwnerAidProductRepository;

    @Autowired
    private ReimbursementStatusRepository reimbursementStatusRepository;

    @Autowired
    private ReimbursementAttachmentRepository reimbursementAttachmentRepository;

    @Autowired
    private ReimbursementAttachmentTypeRepository reimbursementAttachmentTypeRepository;

    @Autowired
    private ReimbursementTraineeAttachmentRepository reimbursementTraineeAttachmentRepository;

    @Autowired
    private ReimbursementTraineeRepository reimbursementTraineeRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    //STATIC CONSTRUCTOR

    static{
        statusMap = new HashMap<>();
        Set<String> set;

                //FROM NULL
        set = new HashSet<>();
        set.add(ReimbursementStatus.ANNOUNCED_CODE);
        set.add(ReimbursementStatus.CORRECTED_CODE);
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        statusMap.put(null, set);

        //FROM ANNOUNCED
        set = new HashSet<>();
        set.add(ReimbursementStatus.CORRECTED_CODE);
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        statusMap.put(ReimbursementStatus.ANNOUNCED_CODE, set);

        //FROM CORRECTED
        set = new HashSet<>();
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        statusMap.put(ReimbursementStatus.CORRECTED_CODE, set);

        //FROM TO_VERIFY
        set = new HashSet<>();
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        statusMap.put(ReimbursementStatus.TO_VERIFY_CODE, set);

        //FROM TO REIMB
        set = new HashSet<>();
        set.add(ReimbursementStatus.EXPORTED_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        statusMap.put(ReimbursementStatus.TO_REIMB_CODE, set);

        //FROM EXPORTED
        set = new HashSet<>();
        set.add(ReimbursementStatus.FINISHED_CODE);
        statusMap.put(ReimbursementStatus.EXPORTED_CODE, set);

        //FROM FINISHED
        set = new HashSet<>();
        statusMap.put(ReimbursementStatus.FINISHED_CODE, set);

        //FROM CANCELLED
        set = new HashSet<>();
        statusMap.put(ReimbursementStatus.CANCELLED_CODE, set);
    }

    //PUBLIC METHODS

    @Override
    public ReimbursementDTO findReimbursement(Long id) {
        Reimbursement reimbursement = reimbursementRepository.get(id);
        return ReimbursementDTO.create(reimbursement);
    }

    @Override
    public ReimbursementDTO createInitialReimbursement(Long reimbursementDeliveryId) {
        ReimbursementDelivery reimbursementDelivery = reimbursementDeliveryRepository.get(reimbursementDeliveryId);
        ReimbursementPattern reimbursementPattern = reimbursementDelivery.getReimbursementPattern();

        String reimbursementDelay = reimbursementPatternService.findReimbursementPatternParam(reimbursementPattern.getId(), ReimbursementPatternParam.REIMBDELAY);
        Date reimbursementDate = gryfPLSQLRepository.getNthBusinessDay(reimbursementDelivery.getReimbursementAnnouncementDate(), Integer.valueOf(reimbursementDelay));

        return ReimbursementDTO.createInitial(reimbursementDelivery, reimbursementDate);
    }

    @Override
    public Long saveReimbursement(ReimbursementDTO dto) {

        try{
            Reimbursement entity;

            if(dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.ANNOUNCED_CODE, true);
            }else{
                entity = reimbursementRepository.get(dto.getId());
                validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE,
                                             ReimbursementStatus.TO_VERIFY_CODE, ReimbursementStatus.TO_REIMB_CODE,
                                             ReimbursementStatus.EXPORTED_CODE);
                entity = update(entity, dto, entity.getStatus().getStatusId(), true);
            }
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public Long correctReimbursement(ReimbursementDTO dto) {
        try{
            Reimbursement entity;

            if(dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.CORRECTED_CODE, true,
                                            ValidationGroupReimbursementCorrect.class);
            }else{
                entity = reimbursementRepository.get(dto.getId());
                validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE);
                entity = update(entity, dto, ReimbursementStatus.CORRECTED_CODE, true,
                                            ValidationGroupReimbursementCorrect.class);
            }

            correctReimbursement(entity);
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public Long verifyReimbursement(ReimbursementDTO dto) {
        try {
            Reimbursement entity;

            if (dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.TO_VERIFY_CODE, true,
                        ValidationGroupReimbursementSettleAndVerify.class);
            } else {
                entity = reimbursementRepository.get(dto.getId());
                validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE);
                entity = update(entity, dto, ReimbursementStatus.TO_VERIFY_CODE, true,
                        ValidationGroupReimbursementSettleAndVerify.class);
            }

            settleReimbursement(entity);
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public Long settleReimbursement(ReimbursementDTO dto) {
        try{
            Reimbursement entity;

            if(dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.TO_REIMB_CODE, true,
                                            ValidationGroupReimbursementSettleAndVerify.class);
            }else{
                entity = reimbursementRepository.get(dto.getId());
                validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE, ReimbursementStatus.TO_REIMB_CODE);
                entity = update(entity, dto, ReimbursementStatus.TO_REIMB_CODE, true,
                                            ValidationGroupReimbursementSettleAndVerify.class);
            }

            settleReimbursement(entity);
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public Long generateConfirmationReimbursement(ReimbursementDTO dto) {
        if (dto.getId() == null) {
            gryfValidator.validate("Akcja nie jest dostepna dla nowego wniosku");
            return null;
        }

        String reportLocation = null;
        try {
            //UPDATE ENTITY
            Reimbursement entity = reimbursementRepository.get(dto.getId());
            validateStatusAccess(entity, ReimbursementStatus.EXPORTED_CODE);
            entity = update(entity, dto, entity.getStatus().getStatusId(), true, ValidationGroupReimbursementComplete.class);

            //SAVE ATTACHMENT REPORT
            ReimbursementAttachment reportAttachment = new ReimbursementAttachment();
            reportAttachment.setAttachmentType(reimbursementAttachmentTypeRepository.get(ReimbursementAttachmentType.ORIGINAL_CODE));
            reportAttachment.setName("Potwierdzenie realizacji dofinansowania");
            reportAttachment.setOriginalFileName("Potwierdzenie realizacji dofinansowania.pdf");
            entity.addReimbursementAttachment(reportAttachment);
            reimbursementAttachmentRepository.save(reportAttachment);

            //GENERATOE REPORT
            String reportFileName = findAttachmentName(entity, ReimbursementAttachment.ATTACHMENT_TYPE_IN_PATH, reportAttachment.getId(), reportAttachment.getName()) + ".pdf";
            reportLocation = reportService.generateReport(ReportTemplateCode.GRANT_ACKNOWLEDGMENT_REPORT, reportFileName, FileType.REIMBURSEMENTS);

            //UPDATE REPORT PATH
            reportAttachment.setFileLocation(reportLocation);
            reimbursementAttachmentRepository.update(reportAttachment, reportAttachment.getId());

            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            if(!StringUtils.isEmpty(reportLocation)){
                fileService.deleteFile(reportLocation);
            }
            throw e;
        }
    }

    @Override
    public Long completeReimbursement(ReimbursementDTO dto) {
        if(dto.getId() == null) {
            gryfValidator.validate("Akcja nie jest dostepna dla nowego wniosku");
            return null;
        }

        try{
            Reimbursement entity = reimbursementRepository.get(dto.getId());
            validateStatusAccess(entity, ReimbursementStatus.EXPORTED_CODE);
            entity = update(entity, dto, ReimbursementStatus.FINISHED_CODE, true,
                                        ValidationGroupReimbursementComplete.class);
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public Long cancelReimbursement(ReimbursementDTO dto) {
        if(dto.getId() == null) {
            gryfValidator.validate("Akcja nie jest dostepna dla nowego wniosku");
            return null;
        }

        try{
            Reimbursement entity = reimbursementRepository.get(dto.getId());
            validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE,
                                         ReimbursementStatus.TO_VERIFY_CODE, ReimbursementStatus.TO_REIMB_CODE);
            entity = update(entity, dto, ReimbursementStatus.CANCELLED_CODE, false);
            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            List<FileDTO> files = dto.getAllFiles();
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        if(id != null) {
            Reimbursement entity = reimbursementRepository.get(id);
            throw new StaleDataException(entity.getId(), entity);
        }
    }

    //PRIVATE METHODS

    private Reimbursement save(ReimbursementDTO dto, String nextStatus, boolean isValidation, Class ... validationClasses){
        Reimbursement entity = new Reimbursement();
        validateStatusChange(null, nextStatus);
        if(isValidation) {
            validate(dto, entity, validationClasses);
        }
        //SAVE (NAJPIERW PERSIST ABY MIEC ID DO PLIKOW)
        entity = reimbursementRepository.save(entity);
        fillReimbursement(entity, dto, nextStatus);
        return entity;
    }

    private Reimbursement update(Reimbursement entity, ReimbursementDTO dto, String nextStatus, boolean isValidation, Class ... validationClasses){
        validateVersion(entity, dto);
        validateStatusChange(entity.getStatus().getStatusId(), nextStatus);
        if(isValidation) {
            validate(dto, entity, validationClasses);
        }
        //UPDATE (NAJPIERW MERGE ABY BYLO ANALOGICZNIE JA W SAVE)
        entity = reimbursementRepository.update(entity, entity.getId());
        fillReimbursement(entity, dto, nextStatus);
        return entity;
    }

    //PRIVATE METHODS - VALIDATE

    private void validateVersion(Reimbursement entity, ReimbursementDTO dto){
        if(!Objects.equals(entity.getVersion(), dto.getVersion())){
            throw new StaleDataException(entity.getId(), entity);
        }
    }

    private void validateStatusAccess(Reimbursement entity, String ... statuses){
        if(entity.getStatus() != null){
            boolean flag = false;
            for (String status : statuses) {
                if (status.equals(entity.getStatus().getStatusId())) {
                    flag = true;
                }
            }
            if(!flag){
                gryfValidator.validate("Akcja nie jest dostepna w danym statusie");
            }
        }
    }

    private void validateStatusChange(String statusSrc, String statusDes){
        if(statusSrc == null) {
            if(statusDes != null) {
                Set<String> allowedStatuses = statusMap.get(null);
                if (!allowedStatuses.contains(statusDes)) {
                    gryfValidator.validate(String.format("Nie można stworzyć nowego rozliczenia w statusie '%s'",
                            reimbursementStatusRepository.get(statusDes).getStatusName()));
                }
            }
        }
        else if(!Objects.equals(statusSrc, statusDes)) {
            Set<String> allowedStatuses = statusMap.get(statusSrc);
            if (!allowedStatuses.contains(statusDes)) {
                gryfValidator.validate(String.format("Rozliczenie jest w statusie '%s' - z tego statusu nie można przejść do statusu '%s'",
                        reimbursementStatusRepository.get(statusSrc).getStatusName(),
                        (statusDes != null) ? reimbursementStatusRepository.get(statusDes).getStatusName() : "NOWE"));
            }
        }
    }

    private void validate(ReimbursementDTO dto, Reimbursement entity, Class ... classes){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(dto, classes);

        //MAKSUMALNA WIELKOSC PLIKU
        validateFileMaxSize(violations, dto);

        //RACHUNEK BANKOWY
        if(!Objects.equals(entity.getTrainingInstitutionReimbursementAccountNumber(), dto.getTrainingInstitutionReimbursementAccountNumber())){
            if (!securityChecker.hasPrivilege(Privileges.GRF_PBE_REIMB_TI_ACC_MOD)) {
                violations.add(new EntityConstraintViolation(ReimbursementDTO.TRAINING_INSTITUTION_REIMBURSEMENT_ACCOUNT_NUMBER_ATTR_NAME,
                        "Nie posiadasz uprawnień do edycji pola 'Numer rachunku bankowego do zwrotu dla IS'",
                        dto.getTrainingInstitutionReimbursementAccountNumber()));
            }
        }

        //CZY KWOTA BRUTTO FAKTURY == CALKOWITY KOSZT SKOLEŃ
        if(GryfUtils.isAssignableFrom(ValidationGroupReimbursementSettleAndVerify.class, classes)){
            validateTrainingsTotalCost(violations, dto);
        }

        //WYMAGANE ZAŁĄCZNIKI
        boolean requiredValidation = (GryfUtils.isAssignableFrom(ValidationGroupReimbursementSettleAndVerify.class, classes));
        validateAttachments(violations, dto, requiredValidation);

        gryfValidator.validate(violations);
    }

    private void validateFileMaxSize(List<EntityConstraintViolation> violations, ReimbursementDTO dto){

        //VALIDATE REIMBURSEMENT ATTACHMENT
        gryfValidator.addFileMaxSizePrivilege(violations, ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME, dto.getReimbursementAttachments());

        //VALIDATE TRAINEE ATTACHMENT
        List<ReimbursementTrainingDTO> trainingDtoList = dto.getReimbursementTrainings();
        if(trainingDtoList != null) {
            ReimbursementTrainingDTO[] trainingDtoTab = trainingDtoList.toArray(new ReimbursementTrainingDTO[trainingDtoList.size()]);
            for (int i = 0; i < trainingDtoTab.length; i++) {

                List<ReimbursementTraineeDTO> traineeDtoList = trainingDtoTab[i].getReimbursementTrainees();
                if (traineeDtoList != null) {
                    ReimbursementTraineeDTO[] traineeDtoTab = traineeDtoList.toArray(new ReimbursementTraineeDTO[traineeDtoList.size()]);
                    for (int j = 0; j < traineeDtoTab.length; j++) {
                        String prefix = String.format("reimbursementTrainings[%s].reimbursementTrainees[%s].reimbursementTraineeAttachments", i, j);
                        gryfValidator.addFileMaxSizePrivilege(violations, prefix, traineeDtoTab[j].getReimbursementTraineeAttachments());
                    }
                }
            }
        }
    }

    private void validateTrainingsTotalCost(List<EntityConstraintViolation> violations, ReimbursementDTO dto){
        if(dto.getInvoiceAnonGrossAmount() != null) {
            BigDecimal trainingsTotalCost = ReimbursementCalculationHelper.calculateTrainingCostTotal_dependFilledData(dto);
            if (!dto.getInvoiceAnonGrossAmount().equals(trainingsTotalCost)) {
                String message = String.format("Kwota brutto faktury musi być równa całkowitemu kosztowi szkoleń: kwota brutto faktury = %s, całkowity koszt szkolenia = %s",
                                                GryfUtils.amountToString(dto.getInvoiceAnonGrossAmount()), GryfUtils.amountToString(trainingsTotalCost));
                violations.add(new EntityConstraintViolation(ReimbursementDTO.INVOICE_ANON_GROSS_AMOUNT_ATTR_NAME, message, dto.getInvoiceAnonGrossAmount()));
            }
        }
    }

    private void validateAttachments(List<EntityConstraintViolation> violations, ReimbursementDTO dto, boolean requiredValidation){
        ReimbursementDeliverySearchResultDTO deliveryDTO = dto.getReimbursementDelivery();
        if(deliveryDTO != null) {
            ReimbursementDelivery delivery = reimbursementDeliveryRepository.get(deliveryDTO.getId());
            ReimbursementPattern pattern = delivery.getReimbursementPattern();
            if (pattern != null) {

                //ZALACZNIKI DLA ROZLICZEN

                boolean uniqueNameValid = validateUniqueNameAttachments(violations, dto.getReimbursementAttachments());
                if(uniqueNameValid && requiredValidation) {
                    validateRequiredAttachments(violations, pattern.getReimbursementAttachmentRequiredList(), dto.getReimbursementAttachments());
                }

                //ZALACZNIKI DLA UZYTKOWNIKOW
                List<ReimbursementTrainingDTO> trainingDtoList = dto.getReimbursementTrainings();
                if(trainingDtoList != null) {
                    ReimbursementTrainingDTO[] trainingDtoTab = trainingDtoList.toArray(new ReimbursementTrainingDTO[trainingDtoList.size()]);
                    for (int i = 0; i < trainingDtoTab.length; i++) {

                        List<ReimbursementTraineeDTO> traineeDtoList = trainingDtoTab[i].getReimbursementTrainees();
                        if(traineeDtoList != null) {
                            ReimbursementTraineeDTO[] traineeDtoTab = traineeDtoList.toArray(new ReimbursementTraineeDTO[traineeDtoList.size()]);
                            for (int j = 0; j < traineeDtoTab.length; j++) {
                                String prefix = String.format("reimbursementTrainings[%s].reimbursementTrainees[%s]", i, j);
                                boolean uniqueTraineeNameValid = validateUniqueNameTraineeAttachments(violations, prefix, traineeDtoTab[j].getReimbursementTraineeAttachments());
                                if(uniqueTraineeNameValid && requiredValidation) {
                                    validateTraineeRequiredAttachments(violations, prefix, pattern.getReimbursementTraineeAttachmentRequiredList(), traineeDtoTab[j].getReimbursementTraineeAttachments());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sprawdza czy zostały dołaczone wymagane załaczniki. Metoda analogiczna do metody dla załaczników użytkowników
     * ze względu na ewentualną możliowość inengo podejscia w tych przypadkach.
     * @param violations
     * @param attachmentRequiredList
     * @param attachmentDtoList
     */
    private void validateRequiredAttachments(List<EntityConstraintViolation> violations,
                                             List<ReimbursementAttachmentRequired> attachmentRequiredList,
                                             List<ReimbursementAttachmentDTO> attachmentDtoList){

        for (ReimbursementAttachmentRequired requiredAttachment : attachmentRequiredList) {

            boolean isAttachmentInDTO = false;
            ReimbursementAttachmentDTO[] attachmentDtoTab = attachmentDtoList != null ? attachmentDtoList.toArray(
                                                        new ReimbursementAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementAttachmentDTO[0];
            for (int i = 0; i < attachmentDtoTab.length; i++) {

                //ATTACHMENT ISTNIEJE
                if (requiredAttachment.getName().equals(attachmentDtoTab[i].getName())) {

                    //NE DODANO PLIKU
                    if (!attachmentDtoTab[i].isFileIncluded() && StringUtils.isEmpty(attachmentDtoTab[i].getOriginalFileName())) {
                        violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME + "[" + i + "].file",
                                String.format("Nie dodano pliku do wymaganego załacznika o nazwie '%s'", requiredAttachment.getName()), null));
                    }
                    //ZMIENIONO RODZAJ DOKUMENTU
                    String attachmentTypeDto = attachmentDtoTab[i].getAttachmentType() != null ? (String) attachmentDtoTab[i].getAttachmentType().getId() : null;
                    String requiredAttachmentType = requiredAttachment.getAttachmentType() != null ? requiredAttachment.getAttachmentType().getId() : null;
                    if (!Objects.equals(attachmentTypeDto, requiredAttachmentType)) {
                        violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME + "[" + i + "].attachmentType",
                                String.format("Zmieniono rodzaj dokumentu dla wymaganego załacznika o nazwie '%s'", requiredAttachment.getName()), null));
                    }
                    isAttachmentInDTO = true;
                    break;
                }
            }

            //ATTACMENT NIE ISTIEJE
            if (!isAttachmentInDTO) {
                violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME,
                        String.format("Nie dodano wymaganego załacznika o nazwie '%s'", requiredAttachment.getName()), null));
            }
        }
    }

    /**
     * Sprawdza czy zostały dołaczone wymagane załaczniki do użytkowników. Metoda analogiczna do metody dla załaczników rozliczenia
     * ze względu na ewentualną możliowość inengo podejscia w tych przypadkach.
     * @param violations
     * @param attachmentRequiredList
     * @param attachmentDtoList
     */
    private void validateTraineeRequiredAttachments(List<EntityConstraintViolation> violations, String pathPrefix,
                                                    List<ReimbursementTraineeAttachmentRequired> attachmentRequiredList,
                                                    List<ReimbursementTraineeAttachmentDTO> attachmentDtoList){

        for (ReimbursementTraineeAttachmentRequired requiredAttachment : attachmentRequiredList) {

            boolean isAttachmentInDTO = false;
            ReimbursementTraineeAttachmentDTO[] attachmentDtoTab = attachmentDtoList != null ? attachmentDtoList.toArray(
                                   new ReimbursementTraineeAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementTraineeAttachmentDTO[0];
            for (int i = 0; i < attachmentDtoTab.length; i++) {

                //ATTACHMENT ISTNIEJE
                if (requiredAttachment.getName().equals(attachmentDtoTab[i].getName())) {

                    //NE DODANO PLIKU
                    if (!attachmentDtoTab[i].isFileIncluded() && StringUtils.isEmpty(attachmentDtoTab[i].getOriginalFileName())) {
                        violations.add(new EntityConstraintViolation(pathPrefix + ".reimbursementTraineeAttachments[" + i + "].file",
                                String.format("Nie dodano pliku do wymaganego załacznika (dla użytkownika szkolenia) o nazwie '%s'", requiredAttachment.getName()), null));
                    }
                    //ZMIENIONO RODZAJ DOKUMENTU
                    String attachmentTypeDto = attachmentDtoTab[i].getAttachmentType() != null ? (String) attachmentDtoTab[i].getAttachmentType().getId() : null;
                    String requiredAttachmentType = requiredAttachment.getAttachmentType() != null ? requiredAttachment.getAttachmentType().getId() : null;
                    if (!Objects.equals(attachmentTypeDto, requiredAttachmentType)) {
                        violations.add(new EntityConstraintViolation(pathPrefix + ".reimbursementTraineeAttachments[" + i + "].attachmentType",
                                String.format("Zmieniono rodzaj dokumentu dla wymaganego załacznika (dla użytkownika szkolenia) o nazwie '%s'", requiredAttachment.getName()), null));
                    }
                    isAttachmentInDTO = true;
                    break;
                }
            }

            //ATTACMENT NIE ISTIEJE
            if (!isAttachmentInDTO) {
                violations.add(new EntityConstraintViolation(pathPrefix,String.format("Nie dodano wymaganego załacznika o nazwie '%s' dla użytkownika szkolenia", requiredAttachment.getName()), null));
            }
        }
    }

    /**
     * Sprawdza czy nie wsytapiły duplikaty w nazwach załaczników rozliczenia
     * @param violations
     * @param attachmentDtoList
     * @return
     */
    private boolean validateUniqueNameAttachments(List<EntityConstraintViolation> violations,
                                               List<ReimbursementAttachmentDTO> attachmentDtoList){
        boolean flag = true;
        ReimbursementAttachmentDTO[] attachmentDtoTab = attachmentDtoList != null ? attachmentDtoList.toArray(
                new ReimbursementAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementAttachmentDTO[0];
        for (int i = 0; i < attachmentDtoTab.length; i++) {
            final ReimbursementAttachmentDTO attachmentDto = attachmentDtoTab[i];

            if(!StringUtils.isEmpty(attachmentDto.getName())) {

                //OCCURENCE
                int occurrence = GryfUtils.countOccurrence(attachmentDtoList, new GryfUtils.Predicate<ReimbursementAttachmentDTO>() {
                    @Override
                    public boolean apply(ReimbursementAttachmentDTO input) {
                        return Objects.equals(attachmentDto.getName(), input.getName());
                    }
                });
                if (occurrence > 1) {
                    violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME + "[" + i + "].name",
                            String.format("Nazwa załącznika musi być unikalna - nazwa %s występuje w liscie zalacznika %s razy", attachmentDto.getName(), occurrence), null));
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * Sprawdza czy nie wsytapiły duplikaty w nazwach załaczników użytkowników
     * @param violations
     * @param attachmentDtoList
     * @return
     */
    private boolean validateUniqueNameTraineeAttachments(List<EntityConstraintViolation> violations, String pathPrefix,
                                                    List<ReimbursementTraineeAttachmentDTO> attachmentDtoList){
        boolean flag = true;
        ReimbursementTraineeAttachmentDTO[] attachmentDtoTab = attachmentDtoList != null ? attachmentDtoList.toArray(
                new ReimbursementTraineeAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementTraineeAttachmentDTO[0];
        for (int i = 0; i < attachmentDtoTab.length; i++) {
            final ReimbursementTraineeAttachmentDTO attachmentDto = attachmentDtoTab[i];

            if(!StringUtils.isEmpty(attachmentDto.getName())) {

                //OCCURENCE
                int occurrence = GryfUtils.countOccurrence(attachmentDtoList, new GryfUtils.Predicate<ReimbursementTraineeAttachmentDTO>() {
                    @Override
                    public boolean apply(ReimbursementTraineeAttachmentDTO input) {
                        return Objects.equals(attachmentDto.getName(), input.getName());
                    }
                });
                if (occurrence > 1) {
                    violations.add(new EntityConstraintViolation(pathPrefix + "[" + i + "].name",
                            String.format("Nazwa załącznika musi być unikalna - nazwa %s występuje w liście zalacznika użytkownika %s razy",
                                                attachmentDto.getName(), occurrence), null));
                    flag = false;
                }
            }
        }
        return flag;
    }

    //PRIVATE METHODS - LOGIC

    protected String findAttachmentName(Reimbursement reimbursement, String attachmentType, Long attachmentId, String attachmentName){
        ReimbursementDelivery reimbursementDelivery = reimbursement.getReimbursementDelivery();
        String fileName = String.format("%s_%s_%s_%s_%s", reimbursementDelivery.getId(), reimbursement.getId(),
                attachmentType, attachmentId, attachmentName);
        return StringUtils.convertFileName(fileName);
    }

    private Reimbursement fillReimbursement(Reimbursement entity, ReimbursementDTO dto, String nextStatus) {

        //BASIC FIELDS
        switch (nextStatus) {
            case ReimbursementStatus.ANNOUNCED_CODE:
            case ReimbursementStatus.CORRECTED_CODE:
            case ReimbursementStatus.TO_VERIFY_CODE:
            case ReimbursementStatus.TO_REIMB_CODE:
                entity.setReimbursementDate(dto.getReimbursementDate());
                entity.setAnnouncementDate(dto.getAnnouncementDate());
                entity.setInvoiceNumber(dto.getInvoiceNumber());
                entity.setInvoiceAnonGrossAmount(dto.getInvoiceAnonGrossAmount());
                entity.setInvoiceAnonVouchAmount(dto.getInvoiceAnonVouchAmount());
                entity.setTrainingInstitutionReimbursementAccountNumber(dto.getTrainingInstitutionReimbursementAccountNumber());
                entity.setEnterprise(dto.getEnterprise() != null ? enterpriseRepository.get(dto.getEnterprise().getId()) : null);
                entity.setSxoTiAmountDueTotal(dto.getSxoTiAmountDueTotal());
                entity.setSxoEntAmountDueTotal(dto.getSxoEntAmountDueTotal());
                entity.setTransferDate(dto.getTransferDate());

                entity.setRequiredCorrectionDate(dto.getRequiredCorrectionDate());
                entity.setCorrectionDate(dto.getCorrectionDate());
                entity.setCorrectionsNumber(dto.getCorrectionsNumber());
                entity.setCorrectionReason(dto.getCorrectionReason());

                entity.setReimbursementDelivery(dto.getReimbursementDelivery() != null ? reimbursementDeliveryRepository.get(dto.getReimbursementDelivery().getId()) : null);
                organizeTrainingList(entity, dto.getReimbursementTrainings(), true);
                organizeAttachmentList(entity, dto.getReimbursementAttachments(), true);

                entity.setRemarks(dto.getRemarks());
                break;
            default:
                entity.setRemarks(dto.getRemarks());
                break;
        }

        //CUSTOM FIELDS
        switch (nextStatus){
            case ReimbursementStatus.CORRECTED_CODE:
                entity.setCorrectionDate(new Date());
                entity.setCorrectionReason(dto.getCorrectionReason());
                break;
            case ReimbursementStatus.EXPORTED_CODE:
                entity.setTransferDate(dto.getTransferDate());
                organizeAttachmentList(entity, dto.getReimbursementAttachments(), false);
                break;
        }

        //STATUS
        entity.setStatus(reimbursementStatusRepository.get(nextStatus));
        return entity;
    }

    private void correctReimbursement(Reimbursement entity){
        ReimbursementDelivery delivery = entity.getReimbursementDelivery();
        ReimbursementPattern pattern = delivery.getReimbursementPattern();
        String delay = reimbursementPatternService.findReimbursementPatternParam(pattern.getId(), ReimbursementPatternParam.CORRDELAY);

        entity.setCorrectionDate(new Date());
        entity.setRequiredCorrectionDate(GryfUtils.addDays(entity.getCorrectionDate(), Integer.valueOf(delay)));
        entity.setCorrectionsNumber(entity.getCorrectionsNumber() != null ? entity.getCorrectionsNumber() + 1 : 1);
    }

    private void settleReimbursement(Reimbursement entity){
        BigDecimal sumSxoTiAmountDue = BigDecimal.ZERO;
        BigDecimal sumSxoEntAmountDue = BigDecimal.ZERO;

        for (ReimbursementTraining t : entity.getReimbursementTrainings()) {
            t.setProductTotalValue(new BigDecimal("90"));//TODO; przepisać z produktu
            t.setProductAidValue(new BigDecimal("45"));//TODO; przepisać z produktu

            //WGSRB = fMIN (WPROD, CBGS)
            BigDecimal wgsrb = ReimbursementCalculationHelper.calculateVoucherRefundedTrainingHourValue(t);

            //KNIodS = WGSRB * LP
            BigDecimal kniods = ReimbursementCalculationHelper.calculateSxoTiAmountDue(t, wgsrb);
            t.setSxoTiAmountDue(kniods);
            sumSxoEntAmountDue = sumSxoEntAmountDue.add(kniods);

            //KNModS = //MAX(0, (WPROD – CBGS) * LP)
            BigDecimal knmods = ReimbursementCalculationHelper.calculateSxoEntAmountDue(t);
            t.setSxoEntAmountDue(knmods);
            sumSxoTiAmountDue = sumSxoTiAmountDue.add(knmods);
        }

        entity.setSxoTiAmountDueTotal(sumSxoTiAmountDue);
        entity.setSxoEntAmountDueTotal(sumSxoEntAmountDue);
    }

    //PRIVATE METHODS - ATTACHMENT REQUIRED

    private boolean isAttachmentRequired(ReimbursementDelivery delivery, String name){
        ReimbursementPattern pattern = delivery.getReimbursementPattern();
        for (ReimbursementAttachmentRequired attachmentRequired : pattern.getReimbursementAttachmentRequiredList()) {
            if(Objects.equals(attachmentRequired.getName(), name)){
                return true;
            }
        }
        return false;
    }

    private boolean isTraineeAttachmentRequired(ReimbursementDelivery delivery, String name){
        ReimbursementPattern pattern = delivery.getReimbursementPattern();
        for (ReimbursementTraineeAttachmentRequired attachmentRequired : pattern.getReimbursementTraineeAttachmentRequiredList()) {
            if(Objects.equals(attachmentRequired.getName(), name)){
                return true;
            }
        }
        return false;
    }

  //PRIVATE METHODS - ORGANIZE LIST (TRAINING)

    private void fillTraining(Reimbursement reimbursement, ReimbursementTraining training, ReimbursementTrainingDTO trainingDTO, boolean isRemoveAllowed){
        training.setTrainingName(trainingDTO.getTrainingName());
        training.setTrainingDateFrom(trainingDTO.getTrainingDateFrom());
        training.setTrainingDateTo(trainingDTO.getTrainingDateTo());
        training.setTrainingPlace(trainingDTO.getTrainingPlace());
        training.setGrantOwnerAidProduct(trainingDTO.getGrantOwnerAidProductId() != null ?
                grantOwnerAidProductRepository.get(trainingDTO.getGrantOwnerAidProductId()) : null);
        training.setProductsNumber(trainingDTO.getProductsNumber());
        training.setTrainingHourGrossPrice(trainingDTO.getTrainingHourGrossPrice());
        training.setTrainingHoursTotal(trainingDTO.getTrainingHoursTotal());

        reimbursement.addReimbursementTraining(training);
        organizeTraineeList(training, trainingDTO.getReimbursementTrainees(), isRemoveAllowed);
    }

    private void organizeTrainingList(Reimbursement reimbursement, List<ReimbursementTrainingDTO> trainingDtoList, boolean isRemoveAllowed){
        if(trainingDtoList == null){
            trainingDtoList = new ArrayList<>();
        }
        List<ReimbursementTrainingTempDTO> trainingTempDtoList = makeTrainingTempDtoList(reimbursement.getReimbursementTrainings(), trainingDtoList);

        for (ReimbursementTrainingTempDTO trainingTempDTO : trainingTempDtoList) {
            ReimbursementTraining training = trainingTempDTO.getTraining();
            ReimbursementTrainingDTO trainingDTO = trainingTempDTO.getTrainingDTO();

            //UPDATE OLD RECORD
            if(training != null && trainingDTO != null){
                fillTraining(reimbursement, training, trainingDTO, isRemoveAllowed);

            //REMOVE OLD RECORD
            }else if(training != null){
                if(isRemoveAllowed) {
                    reimbursement.removeReimbursementTraining(training);
                }else{
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie szkolenia o nazwie '%s", training.getTrainingName()));
                }

            //ADD NEW RECORD
            } else if (trainingDTO != null){
                training = reimbursementTrainingRepository.save(new ReimbursementTraining());
                fillTraining(reimbursement, training, trainingDTO, isRemoveAllowed);
            }
        }
    }

    private List<ReimbursementTrainingTempDTO> makeTrainingTempDtoList(List<ReimbursementTraining> trainings, List<ReimbursementTrainingDTO> trainingDtoList) {
        List<ReimbursementTrainingTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTraining> trainingMap = GryfUtils.constructMap(trainings, new GryfUtils.MapConstructor<Long, ReimbursementTraining>() {
            public boolean isAddToMap(ReimbursementTraining input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTraining input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTrainingDTO> trainingDtoMap = GryfUtils.constructMap(trainingDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTrainingDTO>() {
            public boolean isAddToMap(ReimbursementTrainingDTO input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTrainingDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementTrainingDTO trainingDTO : trainingDtoList) {
            ReimbursementTrainingTempDTO temp = new ReimbursementTrainingTempDTO();
            temp.setTrainingDTO(trainingDTO);
            temp.setTraining((trainingDTO.getId() != null) ? trainingMap.get(trainingDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTraining training : trainings) {
            if(!trainingDtoMap.containsKey(training.getId())){
                ReimbursementTrainingTempDTO temp = new ReimbursementTrainingTempDTO();
                temp.setTraining(training);
                result.add(temp);
            }
        }

        return result;
    }

    private class ReimbursementTrainingTempDTO {

        private ReimbursementTraining training;

        private ReimbursementTrainingDTO trainingDTO;

        public ReimbursementTraining getTraining() {
            return training;
        }

        public void setTraining(ReimbursementTraining training) {
            this.training = training;
        }

        public ReimbursementTrainingDTO getTrainingDTO() {
            return trainingDTO;
        }

        public void setTrainingDTO(ReimbursementTrainingDTO trainingDTO) {
            this.trainingDTO = trainingDTO;
        }
    }

    //PRIVATE METHODS - ORGANIZE LIST (TRAINEE)

    private void fillTrainee(ReimbursementTraining training, ReimbursementTrainee trainee, ReimbursementTraineeDTO traineeDTO, boolean isRemoveAllowed){
        trainee.setTraineeName(traineeDTO.getTraineeName());
        trainee.setTraineeSex(traineeDTO.getTraineeSex() != null ? Sex.valueOf((String) traineeDTO.getTraineeSex().getId()) : null);

        training.addReimbursementTrainee(trainee);
        organizeTraineeAttachmentList(trainee, traineeDTO.getReimbursementTraineeAttachments(), isRemoveAllowed);
    }

    private void organizeTraineeList(ReimbursementTraining training, List<ReimbursementTraineeDTO> traineeDtoList, boolean isRemoveAllowed){
        if(traineeDtoList == null){
            traineeDtoList = new ArrayList<>();
        }
        List<ReimbursementTraineeTempDTO> traineeTempDtoList = makeTraineeTempDtoList(training.getReimbursementTrainees(), traineeDtoList);

        for (ReimbursementTraineeTempDTO traineeTempDTO : traineeTempDtoList) {
            ReimbursementTrainee trainee = traineeTempDTO.getTrainee();
            ReimbursementTraineeDTO traineeDTO = traineeTempDTO.getTraineeDTO();

            //UPDATE OLD RECORD
            if(trainee != null && traineeDTO != null){
                fillTrainee(training, trainee, traineeDTO, isRemoveAllowed);

                //REMOVE OLD RECORD
            }else if(trainee != null){
                if(isRemoveAllowed) {
                    training.removeReimbursementTrainee(trainee);
                }else{
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie użytkownika o nazwie '%s", trainee.getTraineeName()));
                }

                //ADD NEW RECORD
            } else if (traineeDTO != null) {
                trainee = reimbursementTraineeRepository.save(new ReimbursementTrainee());
                fillTrainee(training, trainee, traineeDTO, isRemoveAllowed);
            }
        }
    }

    private List<ReimbursementTraineeTempDTO> makeTraineeTempDtoList(List<ReimbursementTrainee> trainees, List<ReimbursementTraineeDTO> traineeDtoList) {
        List<ReimbursementTraineeTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTrainee> traineeMap = GryfUtils.constructMap(trainees, new GryfUtils.MapConstructor<Long, ReimbursementTrainee>() {
            public boolean isAddToMap(ReimbursementTrainee input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTrainee input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTraineeDTO> traineeDtoMap = GryfUtils.constructMap(traineeDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTraineeDTO>() {
            public boolean isAddToMap(ReimbursementTraineeDTO input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTraineeDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementTraineeDTO traineeDTO : traineeDtoList) {
            ReimbursementTraineeTempDTO temp = new ReimbursementTraineeTempDTO();
            temp.setTraineeDTO(traineeDTO);
            temp.setTrainee((traineeDTO.getId() != null) ? traineeMap.get(traineeDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTrainee trainee : trainees) {
            if(!traineeDtoMap.containsKey(trainee.getId())){
                ReimbursementTraineeTempDTO temp = new ReimbursementTraineeTempDTO();
                temp.setTrainee(trainee);
                result.add(temp);
            }
        }

        return result;
    }

    private class ReimbursementTraineeTempDTO {

        private ReimbursementTrainee trainee;

        private ReimbursementTraineeDTO traineeDTO;

        public ReimbursementTrainee getTrainee() {
            return trainee;
        }

        public void setTrainee(ReimbursementTrainee trainee) {
            this.trainee = trainee;
        }

        public ReimbursementTraineeDTO getTraineeDTO() {
            return traineeDTO;
        }

        public void setTraineeDTO(ReimbursementTraineeDTO traineeDTO) {
            this.traineeDTO = traineeDTO;
        }
    }

    //PRIVATE METHODS - ORGANIZE LIST (ATTACHMENT)

    private void saveAttachment(Reimbursement reimbursement, ReimbursementAttachment attachment, ReimbursementAttachmentDTO attachmentDTO){
        if (attachmentDTO.getFile() != null) {
            FileDTO fileDTO = attachmentDTO.getFile();
            String fileName = findAttachmentName(reimbursement, ReimbursementAttachment.ATTACHMENT_TYPE_IN_PATH,
                    attachment.getId(), attachment.getName());
            String fileLocation = fileService.writeFile(FileType.REIMBURSEMENTS, fileName, fileDTO, attachment);
            if (attachment.getFileLocation() != null && !fileLocation.equals(attachment.getFileLocation())) {
                fileService.deleteFile(attachment.getFileLocation());
            }
            attachment.setFileLocation(fileLocation);
            attachment.setOriginalFileName(fileDTO.getOriginalFilename());
            attachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
            attachmentDTO.setFileIncluded(false);
        }
    }

    private void fillAttachment(Reimbursement reimbursement, ReimbursementAttachment attachment, ReimbursementAttachmentDTO attachmentDTO){
        attachment.setName(attachmentDTO.getName());
        attachment.setAttachmentType(attachmentDTO.getAttachmentType() != null ? reimbursementAttachmentTypeRepository.get((String) attachmentDTO.getAttachmentType().getId()) : null);
        attachment.setRemarks(attachmentDTO.getRemarks());
        attachment.setRequired(YesNo.fromBoolean(isAttachmentRequired(reimbursement.getReimbursementDelivery(), attachment.getName())));

        reimbursement.addReimbursementAttachment(attachment);
    }

    private void organizeAttachmentList(Reimbursement reimbursement, List<ReimbursementAttachmentDTO> attachmentDtoList, boolean isRemoveAllowed){
        if(attachmentDtoList == null){
            attachmentDtoList = new ArrayList<>();
        }
        List<ReimbursementAttachmentTempDTO> attachmentTempDtoList = makeAttachmentTempDtoList(reimbursement.getReimbursementAttachments(), attachmentDtoList);

        for (ReimbursementAttachmentTempDTO attachmentTempDTO : attachmentTempDtoList) {
            ReimbursementAttachment attachment = attachmentTempDTO.getAttachment();
            ReimbursementAttachmentDTO attachmentDTO = attachmentTempDTO.getAttachmentDTO();

            //UPDATE OLD RECORD
            if(attachment != null && attachmentDTO != null) {
                fillAttachment(reimbursement, attachment, attachmentDTO);
                saveAttachment(reimbursement, attachment, attachmentDTO);

            //REMOVE OLD RECORD
            }else if(attachment != null){
                if(isRemoveAllowed) {
                    reimbursement.removeReimbursementAttachment(attachment);
                    fileService.deleteFile(attachment.getFileLocation());
                }else{
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie załącznika o nazwie '%s", attachment.getName()));
                }

            //ADD NEW RECORD
            }else if(attachmentDTO != null){
                attachment = reimbursementAttachmentRepository.save(new ReimbursementAttachment());
                fillAttachment(reimbursement, attachment, attachmentDTO);
                saveAttachment(reimbursement, attachment, attachmentDTO);
            }
        }
    }

    private List<ReimbursementAttachmentTempDTO> makeAttachmentTempDtoList(List<ReimbursementAttachment> attachments, List<ReimbursementAttachmentDTO> attachmentDtoList) {
        List<ReimbursementAttachmentTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementAttachment> attachmentMap = GryfUtils.constructMap(attachments, new GryfUtils.MapConstructor<Long, ReimbursementAttachment>() {
            public boolean isAddToMap(ReimbursementAttachment input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementAttachment input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementAttachmentDTO> attachmentDtoMap = GryfUtils.constructMap(attachmentDtoList, new GryfUtils.MapConstructor<Long, ReimbursementAttachmentDTO>() {
            public boolean isAddToMap(ReimbursementAttachmentDTO input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementAttachmentDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementAttachmentDTO attachmentDTO : attachmentDtoList) {
            ReimbursementAttachmentTempDTO temp = new ReimbursementAttachmentTempDTO();
            temp.setAttachmentDTO(attachmentDTO);
            temp.setAttachment((attachmentDTO.getId() != null) ? attachmentMap.get(attachmentDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementAttachment attachment : attachments) {
            if(!attachmentDtoMap.containsKey(attachment.getId())){
                ReimbursementAttachmentTempDTO temp = new ReimbursementAttachmentTempDTO();
                temp.setAttachment(attachment);
                result.add(temp);
            }
        }

        return result;
    }

    private class ReimbursementAttachmentTempDTO {

        private ReimbursementAttachment attachment;

        private ReimbursementAttachmentDTO attachmentDTO;

        public ReimbursementAttachment getAttachment() {
            return attachment;
        }

        public void setAttachment(ReimbursementAttachment attachment) {
            this.attachment = attachment;
        }

        public ReimbursementAttachmentDTO getAttachmentDTO() {
            return attachmentDTO;
        }

        public void setAttachmentDTO(ReimbursementAttachmentDTO attachmentDTO) {
            this.attachmentDTO = attachmentDTO;
        }
    }

    //PRIVATE METHODS - ORGANIZE LIST (TRAINEE ATTACHMENT)

    private void saveTraineeAttachment(ReimbursementTrainee trainee, ReimbursementTraineeAttachment traineeAttachment, ReimbursementTraineeAttachmentDTO traineeAttachmentDTO){
        if (traineeAttachmentDTO.getFile() != null) {
            FileDTO fileDTO = traineeAttachmentDTO.getFile();
            String fileName = findAttachmentName(trainee.getReimbursementTraining().getReimbursement(), ReimbursementTraineeAttachment.ATTACHMENT_TYPE_IN_PATH,
                    traineeAttachment.getId(), traineeAttachment.getName());
            String fileLocation = fileService.writeFile(FileType.REIMBURSEMENTS, fileName, fileDTO, traineeAttachment);
            if (traineeAttachment.getFileLocation() != null && !fileLocation.equals(traineeAttachment.getFileLocation())) {
                fileService.deleteFile(traineeAttachment.getFileLocation());
            }
            traineeAttachment.setFileLocation(fileLocation);
            traineeAttachment.setOriginalFileName(fileDTO.getOriginalFilename());
            traineeAttachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
            traineeAttachmentDTO.setFileIncluded(false);
        }
    }

    private void fillTraineeAttachment(ReimbursementTrainee trainee, ReimbursementTraineeAttachment attachment, ReimbursementTraineeAttachmentDTO attachmentDTO){
        attachment.setName(attachmentDTO.getName());
        attachment.setAttachmentType(attachmentDTO.getAttachmentType() != null ? reimbursementAttachmentTypeRepository.get((String) attachmentDTO.getAttachmentType().getId()) : null);
        attachment.setRemarks(attachmentDTO.getRemarks());
        attachment.setRequired(YesNo.fromBoolean(isTraineeAttachmentRequired(trainee.getReimbursementTraining().getReimbursement().getReimbursementDelivery(), attachment.getName())));
        trainee.addReimbursementTraineeAttachment(attachment);
    }

    private void organizeTraineeAttachmentList(ReimbursementTrainee trainee, List<ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoList, boolean isRemoveAllowed){
        if(traineeAttachmentDtoList == null){
            traineeAttachmentDtoList = new ArrayList<>();
        }
        List<ReimbursementTraineeAttachmentTempDTO> traineeAttachmentTempDtoList = makeTraineeAttachmentTempDtoList(trainee.getReimbursementTraineeAttachments(), traineeAttachmentDtoList);

        for (ReimbursementTraineeAttachmentTempDTO traineeAttachmentTempDTO : traineeAttachmentTempDtoList) {
            ReimbursementTraineeAttachment traineeAttachment = traineeAttachmentTempDTO.getTraineeAttachment();
            ReimbursementTraineeAttachmentDTO traineeAttachmentDTO = traineeAttachmentTempDTO.getTraineeAttachmentDTO();

            //UPDATE OLD RECORD
            if(traineeAttachment != null && traineeAttachmentDTO != null){
                fillTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
                saveTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);

            //REMOVE OLD RECORD
            }else if(traineeAttachment != null){
                if(isRemoveAllowed) {
                    trainee.removeReimbursementTraineeAttachment(traineeAttachment);
                    fileService.deleteFile(traineeAttachment.getFileLocation());
                }else{
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie załącznika (dla użytkowników) o nazwie '%s", traineeAttachment.getName()));
                }

            //ADD NEW RECORD
            }else if(traineeAttachmentDTO != null){
                traineeAttachment = reimbursementTraineeAttachmentRepository.save(new ReimbursementTraineeAttachment());
                fillTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
                saveTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
            }
        }
    }

    private List<ReimbursementTraineeAttachmentTempDTO> makeTraineeAttachmentTempDtoList(List<ReimbursementTraineeAttachment> traineeAttachments, List<ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoList) {
        List<ReimbursementTraineeAttachmentTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTraineeAttachment> traineeAttachmentMap = GryfUtils.constructMap(traineeAttachments, new GryfUtils.MapConstructor<Long, ReimbursementTraineeAttachment>() {
            public boolean isAddToMap(ReimbursementTraineeAttachment input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTraineeAttachment input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoMap = GryfUtils.constructMap(traineeAttachmentDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTraineeAttachmentDTO>() {
            public boolean isAddToMap(ReimbursementTraineeAttachmentDTO input) {
                return input.getId() != null;
            }
            public Long getKey(ReimbursementTraineeAttachmentDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementTraineeAttachmentDTO traineeAttachmentDTO : traineeAttachmentDtoList) {
            ReimbursementTraineeAttachmentTempDTO temp = new ReimbursementTraineeAttachmentTempDTO();
            temp.setTraineeAttachmentDTO(traineeAttachmentDTO);
            temp.setTraineeAttachment((traineeAttachmentDTO.getId() != null) ? traineeAttachmentMap.get(traineeAttachmentDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTraineeAttachment traineeAttachment : traineeAttachments) {
            if(!traineeAttachmentDtoMap.containsKey(traineeAttachment.getId())){
                ReimbursementTraineeAttachmentTempDTO temp = new ReimbursementTraineeAttachmentTempDTO();
                temp.setTraineeAttachment(traineeAttachment);
                result.add(temp);
            }
        }

        return result;
    }

    private class ReimbursementTraineeAttachmentTempDTO {

        private ReimbursementTraineeAttachment traineeAttachment;

        private ReimbursementTraineeAttachmentDTO traineeAttachmentDTO;

        public ReimbursementTraineeAttachment getTraineeAttachment() {
            return traineeAttachment;
        }

        public void setTraineeAttachment(ReimbursementTraineeAttachment traineeAttachment) {
            this.traineeAttachment = traineeAttachment;
        }

        public ReimbursementTraineeAttachmentDTO getTraineeAttachmentDTO() {
            return traineeAttachmentDTO;
        }

        public void setTraineeAttachmentDTO(ReimbursementTraineeAttachmentDTO traineeAttachmentDTO) {
            this.traineeAttachmentDTO = traineeAttachmentDTO;
        }
    }



}
