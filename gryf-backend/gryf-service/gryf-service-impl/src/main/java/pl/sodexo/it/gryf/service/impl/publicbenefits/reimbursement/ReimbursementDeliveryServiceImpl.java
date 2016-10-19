package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementPatternRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementDeliveryService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.searchform.ReimbursementDeliveryEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.validation.VersionableValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement.ReimbursementDeliverySaveType;
import pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement.ReimbursementDeliveryValidator;

import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Service
@Transactional
public class ReimbursementDeliveryServiceImpl implements ReimbursementDeliveryService {

    //FIELDS

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    @Autowired
    private ReimbursementPatternRepository reimbursementPatternRepository;

    @Autowired
    private ReimbursementDeliveryStatusRepository reimbursementDeliveryStatusRepository;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ReimbursementDeliveryEntityMapper reimbursementDeliveryEntityMapper;

    @Autowired
    private ReimbursementDeliveryEntityToSearchResultMapper reimbursementDeliveryEntityToSearchResultMapper;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private ReimbursementDeliveryValidator reimbursementDeliveryValidator;

    @Autowired
    private VersionableValidator versionableValidator;

    //PUBLIC METHODS

    @Override
    public List<ReimbursementDeliverySearchResultDTO> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        List<ReimbursementDelivery> reimbursementDeliveries = reimbursementDeliveryRepository.findReimbursementDeliveries(searchDTO);
        return reimbursementDeliveryEntityToSearchResultMapper.convert(reimbursementDeliveries);
    }
    
    @Override
    public List<ReimbursementDeliverySearchResultDTO> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        List<ReimbursementDelivery> reimbursementDeliveries = reimbursementDeliveryRepository.findReimbursableDeliveries(searchDTO);
        return reimbursementDeliveryEntityToSearchResultMapper.convert(reimbursementDeliveries);
    }    

    @Override
    public ReimbursementDeliveryDTO findReimbursementDelivery(Long id) {
        ReimbursementDelivery reimbursementDelivery = reimbursementDeliveryRepository.get(id);
        return reimbursementDeliveryEntityMapper.convert(reimbursementDelivery);
    }

    @Override
    public Long saveReimbursementDelivery(ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery entity;

        if(dto.getId() == null) {
            ReimbursementDeliverySaveType reimbursementDeliverySaveType = reimbursementDeliveryValidator.validate(dto);
            entity = createReimbursementDelivery(dto, reimbursementDeliverySaveType);
            entity = reimbursementDeliveryRepository.save(entity);
            setStatusOnSave(entity, reimbursementDeliverySaveType);
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

        reimbursementDeliveryValidator.validateSettleReimbursementDelivery(entity);

        updateReimbursementDelivery(entity, dto);
        //TODO: uruchomi wszystkie rozliczenia
        //TODO: zaanonsowane w ramach tej dostawy w „jednej transkacji”

        return entity.getId();
    }

    @Override
    public Long cancelReimbursementDelivery(ReimbursementDeliveryDTO dto) {
        ReimbursementDelivery entity = reimbursementDeliveryRepository.get(dto.getId());

        reimbursementDeliveryValidator.validateCancelReimbursementDelivery(entity);

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
        return dictionaryEntityMapper.convert(patterns);
    }

    //PRIVATE METHODS

    private ReimbursementDelivery createReimbursementDelivery(ReimbursementDeliveryDTO dto, ReimbursementDeliverySaveType reimbursementDeliverySaveType) {
        TrainingInstitutionSearchResultDTO institutionDTO = dto.getTrainingInstitution();
        DictionaryDTO patternDTO = dto.getReimbursementPattern();

        ReimbursementDelivery entity = new ReimbursementDelivery();
        switch (reimbursementDeliverySaveType) {
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

    private void setStatusOnSave(ReimbursementDelivery entity, ReimbursementDeliverySaveType reimbursementDeliverySaveType) {
        if(entity.getStatus() == null) {
            switch (reimbursementDeliverySaveType) {
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
                if(entity.getDeliveryDate() != null && !GryfStringUtils.isEmpty(entity.getWaybillNumber())){
                    entity.setStatus(reimbursementDeliveryStatusRepository.get(ReimbursementDeliveryStatus.DELIVERED_CODE));
                }
            }
        }
    }

    private void updateReimbursementDelivery(ReimbursementDelivery entity, ReimbursementDeliveryDTO dto){
        versionableValidator.validateVersion(entity, dto, entity.getId());

        switch (entity.getStatus().getStatusId()){
            case ReimbursementDeliveryStatus.ORDERED_CODE:
                reimbursementDeliveryValidator.reimbursementUpdateValidate(entity, dto);
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

}
