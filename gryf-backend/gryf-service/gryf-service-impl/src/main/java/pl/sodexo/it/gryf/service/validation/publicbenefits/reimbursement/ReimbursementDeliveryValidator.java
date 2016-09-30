package pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryStatusRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Walidator dla encji ReimbursementDelivery
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class ReimbursementDeliveryValidator {

    @Autowired
    private GryfPLSQLRepository gryfRepository;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    @Autowired
    private ReimbursementDeliveryStatusRepository reimbursementDeliveryStatusRepository;

    @Autowired
    private SecurityChecker securityChecker;

    public ReimbursementDeliverySaveType validate(ReimbursementDeliveryDTO dto) {

        //GET SAVE TYPE AND VALIDATE
        ReimbursementDeliverySaveType reimbursementDeliverySaveType = getSaveType(dto);

        //VALIDATE
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(dto, reimbursementDeliverySaveType.getValidateClass());
        gryfValidator.addInsertablePrivilege(violations, dto);
        switch (reimbursementDeliverySaveType) {
            case REGISTER:
                if (dto.getTrainingInstitution() != null && dto.getReimbursementPattern() != null && dto.getPlannedReceiptDate() != null) {
                    validateRegisterLimit(violations, dto.getReimbursementPattern(), dto.getTrainingInstitution(), dto.getPlannedReceiptDate());
                }
                break;
            case SECONDARY:
                validateSecondaryDelivery(violations, dto);
                break;
        }
        gryfValidator.validate(violations);

        //VALIDATE CONFIRM
        List<EntityConstraintViolation> confirmViolations = new ArrayList<>();
        if (dto.getDeliveryDate() != null && new Date().before(dto.getDeliveryDate())) {
            confirmViolations.add(new EntityConstraintViolation("deliveryDate", "Wprowadzona data otrzymania przesyłki jest przyszła!"));
        }
        gryfValidator.validateWithConfirm(confirmViolations);

        return reimbursementDeliverySaveType;
    }

    private void validateRegisterLimit(List<EntityConstraintViolation> violations, DictionaryDTO reimbursementPattern, TrainingInstitutionSearchResultDTO trainingInstitution,
            Date plannedReceiptDate) {

        //FIND - DATE FROM & DATE TO
        Date date = gryfRepository.getNextBusinessDay(plannedReceiptDate);
        Date dateFrom = GryfUtils.getStartMonth(date);
        Date dateTo = GryfUtils.getEndMonth(date);

        //CHECK
        String registerLimit = reimbursementPatternService.findReimbursementPatternParam((Long) reimbursementPattern.getId(), ReimbursementPatternParam.DELMMLIMIT);
        Long registerCount = reimbursementDeliveryRepository.findRegisterReimbursementDeliveriesCount(trainingInstitution.getId(), dateFrom, dateTo);
        if (registerCount >= Long.valueOf(registerLimit)) {
            violations.add(new EntityConstraintViolation(ReimbursementPatternParam.DELMMLIMIT,
                    String.format("Przekroczono dopuszczalną w okresie rozliczeniowym ilość " + "zamówionych/obsłużonych dostaw dla tej Instytucji Szkoleniowej. Dopuszczalny limit: %s",
                            registerLimit), null));
        }
    }

    private void validateSecondaryDelivery(List<EntityConstraintViolation> violations, ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery masterEntity = reimbursementDeliveryRepository.get(dto.getParentId());

        //CZY ISTNIEJE W BAZIE
        if (masterEntity == null) {
            violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME, "Nie znaleziono dostawy nadrzędnej dla danego id", dto.getParentId()));
        } else {
            //CZY NADRZEDA DOSTAWA JEST JEDNOCZESNIE PODRZEDNA
            if (masterEntity.getMasterReimbursementDelivery() != null) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME,
                        String.format("Wybrana dostawa nadrzędna jest dostawą " + "podrzedną dla innej dostwy [dostawa o id = %s]", masterEntity.getMasterReimbursementDelivery().getId()),
                        dto.getParentId()));
            }

            //CZY NADRZEDNA W STATUSIE ZESKANOWANA
            if (!ReimbursementDeliveryStatus.SCANNED_CODE.equals(masterEntity.getStatus().getStatusId())) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME, String.format("Wybrana dostawa nadrzędna nie znajduje sie w statusie '%s'",
                        reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.SCANNED_CODE).getDictionaryName()), dto.getParentId()));
            }
        }
    }

    private ReimbursementDeliverySaveType getSaveType(ReimbursementDeliveryDTO dto) {
        if (dto.getParentId() != null) {
            return ReimbursementDeliverySaveType.SECONDARY;
        }
        if (dto.getPlannedReceiptDate() != null && dto.getRequestDate() != null && dto.getDeliveryDate() != null) {
            if (dto.getAcceptedViolations() == null || !dto.getAcceptedViolations().contains("NO_SAVE_TYPE")) {
                gryfValidator.validateWithConfirm("NO_SAVE_TYPE", "Podejmujesz próbę przyjęcia dostawy, która nie została zamówiona – uzupełniono planowaną "
                        + "datę odbioru kuponów oraz datę otrzymania przesyłki dla nowej dostawy, czy chcesz kontynuować");
            }
            return ReimbursementDeliverySaveType.DELIVER;
        }
        if (dto.getPlannedReceiptDate() != null && dto.getRequestDate() != null && dto.getDeliveryDate() == null) {
            return ReimbursementDeliverySaveType.REGISTER;
        }
        if (dto.getPlannedReceiptDate() == null && dto.getRequestDate() == null && dto.getDeliveryDate() != null) {
            return ReimbursementDeliverySaveType.DELIVER;
        } else {
            gryfValidator.validate("Nie udało się rozpoznać typu dostawy. Należy wypełnić pole 'Data otrzymania przesyłki' "
                    + "w przypadku rejestracji przyjęcia dostawy albo pola 'Planowana data odbioru kuponów', 'Data ptrzyjęcia zgłoszenia' "
                    + "w przypadku zamawiania kuriera dla Instytucji Szkoleniowej.");
            return null;
        }
    }

    public void reimbursementUpdateValidate(ReimbursementDelivery entity, ReimbursementDeliveryDTO dto) {
        List<EntityConstraintViolation> violations = new ArrayList<>();
        if (!securityChecker.hasPrivilege(Privileges.GRF_PBE_DELIVERIES_ACCEPT)) {
            if (!Objects.equals(dto.getDeliveryDate(), entity.getDeliveryDate())) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.DELIVERY_DATE_ATTR_NAME, "Nie posiadasz uprawnień do edycji pola 'Data otrzymania przesyłki'"));
            }
        }
        if (!securityChecker.hasPrivilege(Privileges.GRF_PBE_DELIVERIES_ACCEPT)) {
            if (!Objects.equals(dto.getWaybillNumber(), entity.getWaybillNumber())) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.WAYBILL_NUMBER_ATTR_NAME, "Nie posiadasz uprawnień do edycji pola 'Numer listu przewozowego'"));
            }
        }
        if (dto.getDeliveryDate() == null && !StringUtils.isEmpty(dto.getWaybillNumber()) || dto.getDeliveryDate() != null && StringUtils.isEmpty(dto.getWaybillNumber())) {
            if (dto.getDeliveryDate() == null) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.DELIVERY_DATE_ATTR_NAME, "Data otrzymania przesyłki nie może być pusta"));
            }
            if (StringUtils.isEmpty(dto.getWaybillNumber())) {
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.WAYBILL_NUMBER_ATTR_NAME, "Numer listu przewozowego nie może być pusty"));
            }
        }
        gryfValidator.validate(violations);

    }

    public void validateSettleReimbursementDelivery(ReimbursementDelivery entity) {
        if (!ReimbursementDeliveryStatus.SCANNED_CODE.equals(entity.getStatus().getStatusId())) {
            gryfValidator.validate(String.format("Nie można rozliczyć dostawy, która nie jest w statusie '%s'", reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.SCANNED_CODE)));
        }
    }

    public void validateCancelReimbursementDelivery(ReimbursementDelivery entity) {
        if (!ReimbursementDeliveryStatus.ORDERED_CODE.equals(entity.getStatus().getStatusId()) && !ReimbursementDeliveryStatus.DELIVERED_CODE.equals(entity.getStatus().getStatusId())) {
            gryfValidator.validate(
                    String.format("Nie można anulować dostawy, która nie jest w statusie '%s' lub '%s'", reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.ORDERED_CODE),
                            reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.DELIVERED_CODE)));
        }
    }
}
