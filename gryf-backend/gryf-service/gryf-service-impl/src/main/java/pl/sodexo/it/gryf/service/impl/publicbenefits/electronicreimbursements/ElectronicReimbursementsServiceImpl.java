package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.exception.NoCalculationParamsException;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.dao.GrantProgramSearchDao;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementStatus;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements.EreimbursementEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.CorrectionValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.ErmbsValidator;

import java.util.Date;
import java.util.List;

/**
 * Serwis implementujący operacje na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Service
@Transactional
public class ElectronicReimbursementsServiceImpl implements ElectronicReimbursementsService {

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private EreimbursementStatusRepository ereimbursementStatusRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private ProductSearchDao productSearchDao;

    @Autowired
    private EreimbursementDtoMapper ereimbursementDtoMapper;

    @Autowired
    private ErmbsValidator ermbsValidator;

    @Autowired
    private CorrectionValidator correctionValidator;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private CorrectionService correctionService;

    @Autowired
    private ErmbsAttachmentService ermbsAttachmentService;

    @Autowired
    private EreimbursementEntityMapper ereimbursementEntityMapper;

    @Autowired
    private GrantProgramSearchDao grantProgramSearchDao;

    @Override
    public List<ElctRmbsDto> findEcltRmbsListByCriteria(ElctRmbsCriteria criteria) {
        return electronicReimbursementsDao.findEcltRmbsListByCriteria(criteria);
    }

    @Override
    @Cacheable(cacheName = "elctRmbsStatuses")
    public List<SimpleDictionaryDto> findElctRmbsStatuses() {
        return electronicReimbursementsDao.findElctRmbsStatuses();
    }

    @Override
    public ElctRmbsHeadDto createRmbsDtoByTrainingInstanceId(Long trainingInstanceId) {
        ElctRmbsHeadDto elctRmbsHeadDto = electronicReimbursementsDao.findEcltRmbsByTrainingInstanceId(trainingInstanceId);
        if(elctRmbsHeadDto != null){
            return elctRmbsHeadDto;
        }
        elctRmbsHeadDto = new ElctRmbsHeadDto();
        elctRmbsHeadDto.setTrainingInstanceId(trainingInstanceId);
        elctRmbsHeadDto.setGrantProgramId(grantProgramSearchDao.findGrantProgramIdByTrainingInstanceId(trainingInstanceId));
        calculateCharges(elctRmbsHeadDto);
        elctRmbsHeadDto.setProducts(productSearchDao.findProductsByTrainingInstanceId(trainingInstanceId));
        return elctRmbsHeadDto;
    }

    @Override
    public ElctRmbsHeadDto findEcltRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findEcltRmbsById(ermbsId);
    }

    @Override
    public Long saveErmbs(ElctRmbsHeadDto elctRmbsHeadDto) {
        calculateCharges(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.NEW_ERMBS));
        setToReimburseStatusForTiInstance(ereimbursement);
        saveAttachments(elctRmbsHeadDto, ereimbursement);
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburse(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        calculateCharges(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ereimbursement.setArrivalDate(new Date());
        ereimbursement.setReimbursementDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), applicationParameters.getBusinessDaysNumberForReimbursement()));
        setToReimburseStatusForTiInstance(ereimbursement);
        saveAttachments(elctRmbsHeadDto, ereimbursement);
        return ereimbursement.getId();
    }

    @Override
    public Long saveErmbsWithCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ermbsAttachmentService.saveErmbsAttachmentsForCorr(elctRmbsHeadDto);
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburseWithCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        calculateCharges(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ermbsAttachmentService.saveErmbsAttachmentsForCorr(elctRmbsHeadDto);
        correctionService.completeCorrection(elctRmbsHeadDto.getLastCorrectionDto().getId());
        return ereimbursement.getId();
    }

    @Override
    public Long sendToCorrect(CorrectionDto correctionDto) {
        Ereimbursement ereimbursement = ereimbursementRepository.get(correctionDto.getErmbsId());
        ermbsValidator.validateToCorrection(ereimbursement);
        correctionValidator.validateCorrection(correctionDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_CORRECT));
        ereimbursement = ereimbursementRepository.save(ereimbursement);
        correctionService.createAndSaveCorrection(correctionDto);
        return ereimbursement.getId();
    }

    private void saveAttachments(ElctRmbsHeadDto elctRmbsHeadDto, Ereimbursement ereimbursement) {
        ElctRmbsHeadDto dtoFromDatabase = ereimbursementEntityMapper.convert(ereimbursement);
        dtoFromDatabase.setAttachments(elctRmbsHeadDto.getAttachments());
        ermbsAttachmentService.saveErmbsAttachments(dtoFromDatabase);
    }

    private Ereimbursement saveErmbsData(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
        ereimbursement = ereimbursement.getId() != null ? ereimbursementRepository.update(ereimbursement, ereimbursement.getId()) : ereimbursementRepository.save(ereimbursement);
        return ereimbursement;
    }

    private void setToReimburseStatusForTiInstance(Ereimbursement ereimbursement) {
        ereimbursement.getTrainingInstance().setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
    }

    private void calculateCharges(ElctRmbsHeadDto elctRmbsHeadDto) {
        CalculationChargesParamsDto params = getCalculationChargesParamsDto(elctRmbsHeadDto);
        elctRmbsHeadDto.calculateChargers(params);
    }

    private CalculationChargesParamsDto getCalculationChargesParamsDto(ElctRmbsHeadDto elctRmbsHeadDto) {
        CalculationChargesParamsDto params = electronicReimbursementsDao.findCalculationChargesParamsForTrInstId(elctRmbsHeadDto.getTrainingInstanceId());
        if (params == null) {
            throw new NoCalculationParamsException();
        }
        return params;
    }
}
