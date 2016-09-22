package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupDeliverReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupRegisterReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupSecondaryReimbursementDelivery;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementPatternRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.dao.api.crud.repository.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.service.api.SecurityCheckerService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementDeliveryService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Service
@Transactional
public class ReimbursementDeliveryServiceImpl implements ReimbursementDeliveryService {

    //FIELDS

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    @Autowired
    private ReimbursementPatternRepository reimbursementPatternRepository;

    @Autowired
    private ReimbursementDeliveryStatusRepository reimbursementDeliveryStatusRepository;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private GryfPLSQLRepository gryfRepository;

    //PUBLIC METHODS

    @Override
    public List<ReimbursementDeliverySearchResultDTO> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        List<ReimbursementDelivery> reimbursementDeliveries = reimbursementDeliveryRepository.findReimbursementDeliveries(searchDTO);
        return ReimbursementDeliverySearchResultDTO.createList(reimbursementDeliveries);
    }
    
    @Override
    public List<ReimbursementDeliverySearchResultDTO> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        List<ReimbursementDelivery> reimbursementDeliveries = reimbursementDeliveryRepository.findReimbursableDeliveries(searchDTO);
        return ReimbursementDeliverySearchResultDTO.createList(reimbursementDeliveries);
    }    

    @Override
    public ReimbursementDeliveryDTO findReimbursementDelivery(Long id) {
        ReimbursementDelivery reimbursementDelivery = reimbursementDeliveryRepository.get(id);
        return ReimbursementDeliveryDTO.create(reimbursementDelivery);
    }

    @Override
    public Long saveReimbursementDelivery(ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery entity;

        if(dto.getId() == null) {
            SaveType saveType = validate(dto);
            entity = createReimbursementDelivery(dto, saveType);
            entity = reimbursementDeliveryRepository.save(entity);
            setStatusOnSave(entity, saveType);
        }else{
            entity = reimbursementDeliveryRepository.get(dto.getId());
            updateReimbursementDelivery(entity, dto);
            setStatusOnSave(entity, null);
        }

        return entity.getId();
    }

    @Override
    public Long settleReimbursementDelivery(ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery entity = reimbursementDeliveryRepository.get(dto.getId());

        if(!ReimbursementDeliveryStatus.SCANNED_CODE.equals(entity.getStatus().getStatusId())){
            validateService.validate(String.format("Nie można rozliczyć dostawy, która nie jest w statusie '%s'",
                    reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.SCANNED_CODE)));
        }

        updateReimbursementDelivery(entity, dto);
        //TODO: uruchomi wszystkie rozliczenia
        //TODO: zaanonsowane w ramach tej dostawy w „jednej transkacji”

        return entity.getId();
    }

    @Override
    public Long cancelReimbursementDelivery(ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery entity = reimbursementDeliveryRepository.get(dto.getId());

        if(!ReimbursementDeliveryStatus.ORDERED_CODE.equals(entity.getStatus().getStatusId()) &&
           !ReimbursementDeliveryStatus.DELIVERED_CODE.equals(entity.getStatus().getStatusId())){
            validateService.validate(String.format("Nie można anulować dostawy, która nie jest w statusie '%s' lub '%s'",
                    reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.ORDERED_CODE),
                    reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.DELIVERED_CODE)));
        }

        updateReimbursementDelivery(entity, dto);
        entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.CANCELLED_CODE));
        return entity.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        if(id != null) {
            ReimbursementDelivery entity = reimbursementDeliveryRepository.get(id);
            throw new StaleDataException(entity.getId(), entity);
        }
    }

    //DICTIONARIES

    @Override
    public List<DictionaryDTO> findReimbursementPatternsDictionaries() {
        List<ReimbursementPattern> patterns = reimbursementPatternRepository.findPatternsByDate(new Date());
        return DictionaryDTO.createList(patterns);
    }

    //PRIVATE METHODS

    private ReimbursementDelivery createReimbursementDelivery(ReimbursementDeliveryDTO dto, SaveType saveType){
        TrainingInstitutionSearchResultDTO institutionDTO = dto.getTrainingInstitution();
        DictionaryDTO patternDTO = dto.getReimbursementPattern();

        ReimbursementDelivery entity = new ReimbursementDelivery();
        switch (saveType){
            case REGISTER:
                entity.setReimbursementPattern(patternDTO != null ? reimbursementPatternRepository.get((Long) patternDTO.getId()) : null);
                entity.setTrainingInstitution(institutionDTO != null ? trainingInstitutionRepository.get(institutionDTO.getId()) : null);
                entity.setPlannedReceiptDate(dto.getPlannedReceiptDate());
                entity.setRequestDate(dto.getRequestDate());
                entity.setDeliveryAddress(dto.getDeliveryAddress());
                entity.setDeliveryZipCode(dto.getDeliveryZipCode());
                entity.setDeliveryCityName(dto.getDeliveryCityName());
                break;
            case DELIVER:
                entity.setReimbursementPattern(patternDTO != null ? reimbursementPatternRepository.get((Long) patternDTO.getId()) : null);
                entity.setTrainingInstitution(institutionDTO != null ? trainingInstitutionRepository.get(institutionDTO.getId()) : null);
                entity.setDeliveryDate(dto.getDeliveryDate());
                entity.setWaybillNumber(dto.getWaybillNumber());
                entity.setReimbursementAnnouncementDate(calculateAnnouncementDate(dto));
                break;
            case SECONDARY:
                ReimbursementDelivery masterEntity = reimbursementDeliveryRepository.get(dto.getParentId());
                entity.setReimbursementPattern(masterEntity.getReimbursementPattern());
                entity.setTrainingInstitution(masterEntity.getTrainingInstitution());
                entity.setPlannedReceiptDate(masterEntity.getPlannedReceiptDate());
                entity.setRequestDate(masterEntity.getRequestDate());
                entity.setDeliveryAddress(masterEntity.getDeliveryAddress());
                entity.setDeliveryZipCode(masterEntity.getDeliveryZipCode());
                entity.setDeliveryCityName(masterEntity.getDeliveryCityName());
                entity.setDeliveryDate(masterEntity.getDeliveryDate());
                entity.setWaybillNumber(masterEntity.getWaybillNumber());
                entity.setMasterReimbursementDelivery(masterEntity);
                break;
        }
        entity.setRemarks(dto.getRemarks());

        return entity;
    }

    private void setStatusOnSave(ReimbursementDelivery entity, SaveType saveType){
        if(entity.getStatus() == null) {
            switch (saveType) {
                case REGISTER:
                    entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.ORDERED_CODE));
                    break;
                case DELIVER:
                    entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.DELIVERED_CODE));
                    break;
                case SECONDARY:
                    entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.SCANNED_CODE));
                    break;
            }
        }
        else{
            if(ReimbursementDeliveryStatus.ORDERED_CODE.equals(entity.getStatus().getStatusId())){
                if(entity.getDeliveryDate() != null && !StringUtils.isEmpty(entity.getWaybillNumber())){
                    entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.DELIVERED_CODE));
                }
            }
        }
    }

    private void updateReimbursementDelivery(ReimbursementDelivery entity, ReimbursementDeliveryDTO dto){
        validateVersion(entity, dto);

        switch (entity.getStatus().getStatusId()){
            case ReimbursementDeliveryStatus.ORDERED_CODE:

                //VALIDATE
                List<EntityConstraintViolation> violations = new ArrayList<>();
                if(!securityCheckerService.hasPrivilege(Privileges.GRF_PBE_DELIVERIES_ACCEPT)){
                    if(!Objects.equals(dto.getDeliveryDate(), entity.getDeliveryDate())) {
                        violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.DELIVERY_DATE_ATTR_NAME,
                                "Nie posiadasz uprawnień do edycji pola 'Data otrzymania przesyłki'"));
                    }
                }
                if(!securityCheckerService.hasPrivilege(Privileges.GRF_PBE_DELIVERIES_ACCEPT)){
                    if(!Objects.equals(dto.getWaybillNumber(), entity.getWaybillNumber())) {
                        violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.WAYBILL_NUMBER_ATTR_NAME,
                                "Nie posiadasz uprawnień do edycji pola 'Numer listu przewozowego'"));
                    }
                }
                if(dto.getDeliveryDate() == null && !StringUtils.isEmpty(dto.getWaybillNumber()) ||
                        dto.getDeliveryDate() != null && StringUtils.isEmpty(dto.getWaybillNumber())){
                    if(dto.getDeliveryDate() == null){
                        violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.DELIVERY_DATE_ATTR_NAME,
                                "Data otrzymania przesyłki nie może być pusta"));
                    }
                    if(StringUtils.isEmpty(dto.getWaybillNumber())){
                        violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.WAYBILL_NUMBER_ATTR_NAME,
                                "Numer listu przewozowego nie może być pusty"));
                    }
                }
                validateService.validate(violations);

                //UPDATE
                entity.setDeliveryDate(dto.getDeliveryDate());
                entity.setWaybillNumber(dto.getWaybillNumber());
                entity.setRemarks(dto.getRemarks());
                break;
            default:
                entity.setRemarks(dto.getRemarks());
                break;
        }
    }

    //PRIVATE METHODS - VALIDATE

    private void validateVersion(ReimbursementDelivery entity, ReimbursementDeliveryDTO dto){
        if(!Objects.equals(entity.getVersion(), dto.getVersion())){
            throw new StaleDataException(entity.getId(), entity);
        }
    }

    private SaveType validate(ReimbursementDeliveryDTO dto){

        //GET SAVE TYPE AND VALIDATE
        SaveType saveType = getSaveType(dto);

        //VALIDATE
        List<EntityConstraintViolation> violations = validateService.generateViolation(dto, saveType.getValidateClass());
        validateService.addInsertablePrivilege(violations, dto);
        switch(saveType){
            case REGISTER:
                if(dto.getTrainingInstitution() != null && dto.getReimbursementPattern() != null && dto.getPlannedReceiptDate() != null){
                    validateRegisterLimit(violations, dto.getReimbursementPattern(), dto.getTrainingInstitution(), dto.getPlannedReceiptDate());
                }
                break;
            case SECONDARY:
                validateSecondaryDelivery(violations, dto);
                break;
        }
        validateService.validate(violations);

        //VALIDATE CONFIRM
        List<EntityConstraintViolation> confirmViolations = new ArrayList<>();
        if(dto.getDeliveryDate() != null && new Date().before(dto.getDeliveryDate())) {
            confirmViolations.add(new EntityConstraintViolation("deliveryDate", "Wprowadzona data otrzymania przesyłki jest przyszła!"));
        }
        validateService.validateWithConfirm(confirmViolations);


        return saveType;
    }

    private SaveType getSaveType(ReimbursementDeliveryDTO dto){
        if(dto.getParentId() != null){
            return SaveType.SECONDARY;
        }
        if(dto.getPlannedReceiptDate() != null && dto.getRequestDate() != null && dto.getDeliveryDate() != null){
            if(dto.getAcceptedViolations() == null || !dto.getAcceptedViolations().contains("NO_SAVE_TYPE")){
                validateService.validateWithConfirm("NO_SAVE_TYPE", "Podejmujesz próbę przyjęcia dostawy, która nie została zamówiona – uzupełniono planowaną " +
                                                    "datę odbioru kuponów oraz datę otrzymania przesyłki dla nowej dostawy, czy chcesz kontynuować");
            }
            return SaveType.DELIVER;
        }
        if(dto.getPlannedReceiptDate() != null && dto.getRequestDate() != null && dto.getDeliveryDate() == null){
            return SaveType.REGISTER;
        }
        if(dto.getPlannedReceiptDate() == null && dto.getRequestDate() == null && dto.getDeliveryDate() != null){
            return SaveType.DELIVER;
        }
        else{
            validateService.validate("Nie udało się rozpoznać typu dostawy. Należy wypełnić pole 'Data otrzymania przesyłki' " +
                    "w przypadku rejestracji przyjęcia dostawy albo pola 'Planowana data odbioru kuponów', 'Data ptrzyjęcia zgłoszenia' " +
                    "w przypadku zamawiania kuriera dla Instytucji Szkoleniowej.");
            return null;
        }
    }

    private void validateRegisterLimit(List<EntityConstraintViolation> violations, DictionaryDTO reimbursementPattern,
                                       TrainingInstitutionSearchResultDTO trainingInstitution, Date plannedReceiptDate){

        //FIND - DATE FROM & DATE TO
        Date date = gryfRepository.getNextBusinessDay(plannedReceiptDate);
        Date dateFrom = GryfUtils.getStartMonth(date);
        Date dateTo = GryfUtils.getEndMonth(date);

        //CHECK
        String registerLimit = reimbursementPatternService.findReimbursementPatternParam((Long) reimbursementPattern.getId(), ReimbursementPatternParam.DELMMLIMIT);
        Long registerCount = reimbursementDeliveryRepository.findRegisterReimbursementDeliveriesCount(trainingInstitution.getId(), dateFrom, dateTo);
        if(registerCount >= Long.valueOf(registerLimit)){
            violations.add(new EntityConstraintViolation(ReimbursementPatternParam.DELMMLIMIT, String.format("Przekroczono dopuszczalną w okresie rozliczeniowym ilość " +
                    "zamówionych/obsłużonych dostaw dla tej Instytucji Szkoleniowej. Dopuszczalny limit: %s", registerLimit), null));
        }
    }

    private void validateSecondaryDelivery(List<EntityConstraintViolation> violations, ReimbursementDeliveryDTO dto){
        ReimbursementDelivery masterEntity = reimbursementDeliveryRepository.get(dto.getParentId());

        //CZY ISTNIEJE W BAZIE
        if(masterEntity == null){
            violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME,
                                                            "Nie znaleziono dostawy nadrzędnej dla danego id", dto.getParentId()));
        }
        else{
            //CZY NADRZEDA DOSTAWA JEST JEDNOCZESNIE PODRZEDNA
            if(masterEntity.getMasterReimbursementDelivery() != null){
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME, String.format("Wybrana dostawa nadrzędna jest dostawą " +
                                                "podrzedną dla innej dostwy [dostawa o id = %s]", masterEntity.getMasterReimbursementDelivery().getId()), dto.getParentId()));
            }

            //CZY NADRZEDNA W STATUSIE ZESKANOWANA
            if(!ReimbursementDeliveryStatus.SCANNED_CODE.equals(masterEntity.getStatus().getStatusId())){
                violations.add(new EntityConstraintViolation(ReimbursementDeliveryDTO.PARENT_ID_ATTR_NAME, String.format("Wybrana dostawa nadrzędna nie znajduje sie w statusie '%s'",
                        reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.SCANNED_CODE).getDictionaryName()), dto.getParentId()));
            }
        }
    }

    private Date calculateAnnouncementDate(ReimbursementDeliveryDTO dto){
        DictionaryDTO reimbursementPattern = dto.getReimbursementPattern();
        TrainingInstitutionSearchResultDTO trainingInstitution = dto.getTrainingInstitution();
        Date dateFrom = GryfUtils.getStartMonth(dto.getDeliveryDate());
        Date dateTo = GryfUtils.getEndMonth(dto.getDeliveryDate());

        String registerLimit = reimbursementPatternService.findReimbursementPatternParam((Long)reimbursementPattern.getId(), ReimbursementPatternParam.DELMMLIMIT);
        Long registerCount = reimbursementDeliveryRepository.findAnnouncedDeliveryCountInDate(trainingInstitution.getId(), dateFrom, dateTo);
        if(registerCount >= Long.valueOf(registerLimit)){
            Date date = GryfUtils.getNextMonth(dto.getDeliveryDate());
            return GryfUtils.getStartMonth(date);
        }
        return dto.getDeliveryDate();
    }

    //PRIVATE CLASS

    private enum SaveType{

        REGISTER(ValidationGroupRegisterReimbursementDelivery.class),
        DELIVER(ValidationGroupDeliverReimbursementDelivery.class),
        SECONDARY(ValidationGroupSecondaryReimbursementDelivery.class);

        //FIELDS

        private Class<?> validateClass;

        //CONSTRUCTORS

        SaveType(Class<?> validateClass){
            this.validateClass = validateClass;
        }

        //GETTERS

        public Class<?> getValidateClass() {
            return validateClass;
        }
    }
}
