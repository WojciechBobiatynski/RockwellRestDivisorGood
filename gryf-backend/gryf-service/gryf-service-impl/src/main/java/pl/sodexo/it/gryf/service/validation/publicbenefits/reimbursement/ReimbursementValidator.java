package pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementStatusRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.utils.ReimbursementCalculationHelper;

import java.math.BigDecimal;
import java.util.*;

/**
 * Walidator dla encji Reimbursement
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class ReimbursementValidator {

    private static final Map<String, Set<String>> statusMap;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ReimbursementStatusRepository reimbursementStatusRepository;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    static {
        Map<String, Set<String>> tempStatusMap = new HashMap<>();
        Set<String> set;

        //FROM NULL
        set = new HashSet<>();
        set.add(ReimbursementStatus.ANNOUNCED_CODE);
        set.add(ReimbursementStatus.CORRECTED_CODE);
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        tempStatusMap.put(null, Collections.unmodifiableSet(set));

        //FROM ANNOUNCED
        set = new HashSet<>();
        set.add(ReimbursementStatus.CORRECTED_CODE);
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        tempStatusMap.put(ReimbursementStatus.ANNOUNCED_CODE, Collections.unmodifiableSet(set));

        //FROM CORRECTED
        set = new HashSet<>();
        set.add(ReimbursementStatus.TO_VERIFY_CODE);
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        tempStatusMap.put(ReimbursementStatus.CORRECTED_CODE, Collections.unmodifiableSet(set));

        //FROM TO_VERIFY
        set = new HashSet<>();
        set.add(ReimbursementStatus.TO_REIMB_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        tempStatusMap.put(ReimbursementStatus.TO_VERIFY_CODE, Collections.unmodifiableSet(set));

        //FROM TO REIMB
        set = new HashSet<>();
        set.add(ReimbursementStatus.EXPORTED_CODE);
        set.add(ReimbursementStatus.CANCELLED_CODE);
        tempStatusMap.put(ReimbursementStatus.TO_REIMB_CODE, Collections.unmodifiableSet(set));

        //FROM EXPORTED
        set = new HashSet<>();
        set.add(ReimbursementStatus.FINISHED_CODE);
        tempStatusMap.put(ReimbursementStatus.EXPORTED_CODE, Collections.unmodifiableSet(set));

        //FROM FINISHED
        set = new HashSet<>();
        tempStatusMap.put(ReimbursementStatus.FINISHED_CODE, Collections.unmodifiableSet(set));

        //FROM CANCELLED
        set = new HashSet<>();
        tempStatusMap.put(ReimbursementStatus.CANCELLED_CODE, Collections.unmodifiableSet(set));

        statusMap = Collections.unmodifiableMap(tempStatusMap);
    }

    public void validateStatusAccess(Reimbursement entity, String... statuses) {
        if (entity.getStatus() != null) {
            boolean flag = false;
            for (String status : statuses) {
                if (status.equals(entity.getStatus().getStatusId())) {
                    flag = true;
                }
            }
            if (!flag) {
                gryfValidator.validate("Akcja nie jest dostepna w danym statusie");
            }
        }
    }

    public void validateStatusChange(String statusSrc, String statusDes) {
        if (statusSrc == null) {
            if (statusDes != null) {
                Set<String> allowedStatuses = statusMap.get(null);
                if (!allowedStatuses.contains(statusDes)) {
                    gryfValidator.validate(String.format("Nie można stworzyć nowego rozliczenia w statusie '%s'", reimbursementStatusRepository.get(statusDes).getStatusName()));
                }
            }
        } else if (!Objects.equals(statusSrc, statusDes)) {
            Set<String> allowedStatuses = statusMap.get(statusSrc);
            if (!allowedStatuses.contains(statusDes)) {
                gryfValidator.validate(
                        String.format("Rozliczenie jest w statusie '%s' - z tego statusu nie można przejść do statusu '%s'", reimbursementStatusRepository.get(statusSrc).getStatusName(),
                                (statusDes != null) ? reimbursementStatusRepository.get(statusDes).getStatusName() : "NOWE"));
            }
        }
    }

    public void validate(ReimbursementDTO dto, Reimbursement entity, Class... classes) {
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(dto, classes);

        //MAKSUMALNA WIELKOSC PLIKU
        validateFileMaxSize(violations, dto);

        //RACHUNEK BANKOWY
        if (!Objects.equals(entity.getTrainingInstitutionReimbursementAccountNumber(), dto.getTrainingInstitutionReimbursementAccountNumber())) {
            if (!securityChecker.hasPrivilege(Privileges.GRF_PBE_REIMB_TI_ACC_MOD)) {
                violations.add(new EntityConstraintViolation(ReimbursementDTO.TRAINING_INSTITUTION_REIMBURSEMENT_ACCOUNT_NUMBER_ATTR_NAME,
                        "Nie posiadasz uprawnień do edycji pola 'Numer rachunku bankowego do zwrotu dla IS'", dto.getTrainingInstitutionReimbursementAccountNumber()));
            }
        }

        //CZY KWOTA BRUTTO FAKTURY == CALKOWITY KOSZT SKOLEŃ
        if (GryfUtils.isAssignableFrom(ValidationGroupReimbursementSettleAndVerify.class, classes)) {
            validateTrainingsTotalCost(violations, dto);
        }

        //WYMAGANE ZAŁĄCZNIKI
        boolean requiredValidation = (GryfUtils.isAssignableFrom(ValidationGroupReimbursementSettleAndVerify.class, classes));
        validateAttachments(violations, dto, requiredValidation);

        gryfValidator.validate(violations);
    }

    private void validateFileMaxSize(List<EntityConstraintViolation> violations, ReimbursementDTO dto) {

        //VALIDATE REIMBURSEMENT ATTACHMENT
        gryfValidator.addFileMaxSizePrivilege(violations, ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME, dto.getReimbursementAttachments());

        //VALIDATE TRAINEE ATTACHMENT
        List<ReimbursementTrainingDTO> trainingDtoList = dto.getReimbursementTrainings();
        if (trainingDtoList != null) {
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

    private void validateTrainingsTotalCost(List<EntityConstraintViolation> violations, ReimbursementDTO dto) {
        if (dto.getInvoiceAnonGrossAmount() != null) {
            BigDecimal trainingsTotalCost = ReimbursementCalculationHelper.calculateTrainingCostTotal_dependFilledData(dto);
            if (!dto.getInvoiceAnonGrossAmount().equals(trainingsTotalCost)) {
                String message = String.format("Kwota brutto faktury musi być równa całkowitemu kosztowi szkoleń: kwota brutto faktury = %s, całkowity koszt szkolenia = %s",
                        GryfUtils.amountToString(dto.getInvoiceAnonGrossAmount()), GryfUtils.amountToString(trainingsTotalCost));
                violations.add(new EntityConstraintViolation(ReimbursementDTO.INVOICE_ANON_GROSS_AMOUNT_ATTR_NAME, message, dto.getInvoiceAnonGrossAmount()));
            }
        }
    }

    private void validateAttachments(List<EntityConstraintViolation> violations, ReimbursementDTO dto, boolean requiredValidation) {
        ReimbursementDeliverySearchResultDTO deliveryDTO = dto.getReimbursementDelivery();
        if (deliveryDTO != null) {
            ReimbursementDelivery delivery = reimbursementDeliveryRepository.get(deliveryDTO.getId());
            ReimbursementPattern pattern = delivery.getReimbursementPattern();
            if (pattern != null) {

                //ZALACZNIKI DLA ROZLICZEN

                boolean uniqueNameValid = validateUniqueNameAttachments(violations, dto.getReimbursementAttachments());
                if (uniqueNameValid && requiredValidation) {
                    validateRequiredAttachments(violations, pattern.getReimbursementAttachmentRequiredList(), dto.getReimbursementAttachments());
                }

                //ZALACZNIKI DLA UZYTKOWNIKOW
                List<ReimbursementTrainingDTO> trainingDtoList = dto.getReimbursementTrainings();
                if (trainingDtoList != null) {
                    ReimbursementTrainingDTO[] trainingDtoTab = trainingDtoList.toArray(new ReimbursementTrainingDTO[trainingDtoList.size()]);
                    for (int i = 0; i < trainingDtoTab.length; i++) {

                        List<ReimbursementTraineeDTO> traineeDtoList = trainingDtoTab[i].getReimbursementTrainees();
                        if (traineeDtoList != null) {
                            ReimbursementTraineeDTO[] traineeDtoTab = traineeDtoList.toArray(new ReimbursementTraineeDTO[traineeDtoList.size()]);
                            for (int j = 0; j < traineeDtoTab.length; j++) {
                                String prefix = String.format("reimbursementTrainings[%s].reimbursementTrainees[%s]", i, j);
                                boolean uniqueTraineeNameValid = validateUniqueNameTraineeAttachments(violations, prefix, traineeDtoTab[j].getReimbursementTraineeAttachments());
                                if (uniqueTraineeNameValid && requiredValidation) {
                                    validateTraineeRequiredAttachments(violations, prefix, pattern.getReimbursementTraineeAttachmentRequiredList(),
                                            traineeDtoTab[j].getReimbursementTraineeAttachments());
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
     *
     * @param violations
     * @param attachmentRequiredList
     * @param attachmentDtoList
     */
    private void validateRequiredAttachments(List<EntityConstraintViolation> violations, List<ReimbursementAttachmentRequired> attachmentRequiredList,
            List<ReimbursementAttachmentDTO> attachmentDtoList) {

        for (ReimbursementAttachmentRequired requiredAttachment : attachmentRequiredList) {

            boolean isAttachmentInDTO = false;
            ReimbursementAttachmentDTO[] attachmentDtoTab =
                    attachmentDtoList != null ? attachmentDtoList.toArray(new ReimbursementAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementAttachmentDTO[0];
            for (int i = 0; i < attachmentDtoTab.length; i++) {

                //ATTACHMENT ISTNIEJE
                if (requiredAttachment.getName().equals(attachmentDtoTab[i].getName())) {

                    //NE DODANO PLIKU
                    if (!attachmentDtoTab[i].isFileIncluded() && GryfStringUtils.isEmpty(attachmentDtoTab[i].getOriginalFileName())) {
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
     *
     * @param violations
     * @param attachmentRequiredList
     * @param attachmentDtoList
     */
    private void validateTraineeRequiredAttachments(List<EntityConstraintViolation> violations, String pathPrefix, List<ReimbursementTraineeAttachmentRequired> attachmentRequiredList,
            List<ReimbursementTraineeAttachmentDTO> attachmentDtoList) {

        for (ReimbursementTraineeAttachmentRequired requiredAttachment : attachmentRequiredList) {

            boolean isAttachmentInDTO = false;
            ReimbursementTraineeAttachmentDTO[] attachmentDtoTab =
                    attachmentDtoList != null ? attachmentDtoList.toArray(new ReimbursementTraineeAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementTraineeAttachmentDTO[0];
            for (int i = 0; i < attachmentDtoTab.length; i++) {

                //ATTACHMENT ISTNIEJE
                if (requiredAttachment.getName().equals(attachmentDtoTab[i].getName())) {

                    //NE DODANO PLIKU
                    if (!attachmentDtoTab[i].isFileIncluded() && GryfStringUtils.isEmpty(attachmentDtoTab[i].getOriginalFileName())) {
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
                violations
                        .add(new EntityConstraintViolation(pathPrefix, String.format("Nie dodano wymaganego załacznika o nazwie '%s' dla użytkownika szkolenia", requiredAttachment.getName()), null));
            }
        }
    }

    /**
     * Sprawdza czy nie wsytapiły duplikaty w nazwach załaczników rozliczenia
     *
     * @param violations
     * @param attachmentDtoList
     * @return
     */
    private boolean validateUniqueNameAttachments(List<EntityConstraintViolation> violations, List<ReimbursementAttachmentDTO> attachmentDtoList) {
        boolean flag = true;
        ReimbursementAttachmentDTO[] attachmentDtoTab =
                attachmentDtoList != null ? attachmentDtoList.toArray(new ReimbursementAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementAttachmentDTO[0];
        for (int i = 0; i < attachmentDtoTab.length; i++) {
            final ReimbursementAttachmentDTO attachmentDto = attachmentDtoTab[i];

            if (!GryfStringUtils.isEmpty(attachmentDto.getName())) {

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
     *
     * @param violations
     * @param attachmentDtoList
     * @return
     */
    private boolean validateUniqueNameTraineeAttachments(List<EntityConstraintViolation> violations, String pathPrefix, List<ReimbursementTraineeAttachmentDTO> attachmentDtoList) {
        boolean flag = true;
        ReimbursementTraineeAttachmentDTO[] attachmentDtoTab =
                attachmentDtoList != null ? attachmentDtoList.toArray(new ReimbursementTraineeAttachmentDTO[attachmentDtoList.size()]) : new ReimbursementTraineeAttachmentDTO[0];
        for (int i = 0; i < attachmentDtoTab.length; i++) {
            final ReimbursementTraineeAttachmentDTO attachmentDto = attachmentDtoTab[i];

            if (!GryfStringUtils.isEmpty(attachmentDto.getName())) {

                //OCCURENCE
                int occurrence = GryfUtils.countOccurrence(attachmentDtoList, new GryfUtils.Predicate<ReimbursementTraineeAttachmentDTO>() {

                    @Override
                    public boolean apply(ReimbursementTraineeAttachmentDTO input) {
                        return Objects.equals(attachmentDto.getName(), input.getName());
                    }
                });
                if (occurrence > 1) {
                    violations.add(new EntityConstraintViolation(pathPrefix + "[" + i + "].name",
                            String.format("Nazwa załącznika musi być unikalna - nazwa %s występuje w liście zalacznika użytkownika %s razy", attachmentDto.getName(), occurrence), null));
                    flag = false;
                }
            }
        }
        return flag;
    }

}
