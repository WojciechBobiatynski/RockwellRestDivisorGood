package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentsDto;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.exception.NoCalculationParamsException;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsAttachmentDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements.ErmbsAttachmentEntityMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private EreimbursementAttachmentRepository ereimbursementAttachmentRepository;

    @Autowired
    private ErmbsAttachmentDtoMapper ermbsAttachmentDtoMapper;

    @Autowired
    private ErmbsAttachmentEntityMapper ermbsAttachmentEntityMapper;

    @Autowired
    private ProductSearchDao productSearchDao;

    @Autowired
    private FileService fileService;

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
        ElctRmbsHeadDto elctRmbsHeadDto = new ElctRmbsHeadDto();
        calculateCharges(elctRmbsHeadDto, trainingInstanceId);
        elctRmbsHeadDto.setProducts(productSearchDao.findProductsByTrainingInstanceId(trainingInstanceId));
        return elctRmbsHeadDto;
    }

    @Override
    public ElctRmbsHeadDto findEcltRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findEcltRmbsById(ermbsId);
    }

    @Override
    public List<ErmbsAttachmentsDto> saveErmbsAttachments(List<ErmbsAttachmentsDto> ermbsAttachmentsDtos) {
        List<ErmbsAttachment> ermbsAttachments = new ArrayList<>();
        for(ErmbsAttachmentsDto ermbsAttachment : ermbsAttachmentsDtos){
            ErmbsAttachment entity = ereimbursementAttachmentRepository.save(ermbsAttachmentDtoMapper.convert(ermbsAttachment));
            String fileName = String.format("%s_%s_%s_%s", ermbsAttachment.getErmbsId(), ermbsAttachment.getCode(), entity.getId(), ermbsAttachment.getFileDTO().getOriginalFilename());
            String newFileName = GryfStringUtils.convertFileName(fileName);
            String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFileDTO(), null);
            ermbsAttachments.add(entity);
            entity.setOrginalFileName(ermbsAttachment.getFileDTO().getOriginalFilename());
            entity.setFileLocation(filePath);
            ereimbursementAttachmentRepository.save(entity);
        }
        return ermbsAttachmentEntityMapper.convert(ermbsAttachments);
    }

    private Ereimbursement createNewEreimbursement() {
        Ereimbursement ereimbursement = new Ereimbursement();
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(GryfConstants.NEW_ERMBS_STATUS_CODE));
        return ereimbursement;
    }

    private void calculateCharges(ElctRmbsHeadDto elctRmbsHeadDto, Long trainingInstanceId) {
        CalculationChargesParamsDto params = electronicReimbursementsDao.findCalculationChargesParamsForTrInstId(trainingInstanceId);
        if (params == null) {
            throw new NoCalculationParamsException();
        }
        calculateSxoAmount(elctRmbsHeadDto, params);
        calculateIndAmount(elctRmbsHeadDto, params);
    }

    private void calculateSxoAmount(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        if (params.getMaxProductInstance() == null) {
            calculateSxoAmountForTrainings(elctRmbsHeadDto, params);
        } else {
            calculateSxoAmountForExam(elctRmbsHeadDto, params);
        }

    }

    private void calculateSxoAmountForTrainings(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        BigDecimal normalizeHourPrice = new BigDecimal(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal realHourPrice = params.getTrainingHourPrice().compareTo(normalizeHourPrice) < 0 ? params.getTrainingHourPrice() : normalizeHourPrice;
        BigDecimal sxoAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(realHourPrice).divide(new BigDecimal(params.getProductInstanceForHour()));
        elctRmbsHeadDto.setSxoIndAmountDueTotal(sxoAmount);
    }

    private void calculateSxoAmountForExam(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        BigDecimal usedProductsAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(params.getProductValue());
        if (usedProductsAmount.compareTo(params.getTrainingPrice()) > 0) {
            elctRmbsHeadDto.setSxoIndAmountDueTotal(params.getTrainingPrice());
        } else {
            elctRmbsHeadDto.setSxoIndAmountDueTotal(usedProductsAmount);
        }

    }

    private void calculateIndAmount(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        if (params.getMaxProductInstance() == null) {
            calculateIndAmountForTraining(elctRmbsHeadDto, params);
        } else {
            calculateIndAmountForExam(elctRmbsHeadDto, params);
        }
    }

    private void calculateIndAmountForTraining(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        BigDecimal normalizedProductHourPrice = new BigDecimal(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal trainingHourDifferenceCost = BigDecimal.ZERO;
        Integer hoursPaidWithCash = params.getTrainingHoursNumber() - params.getUsedProductsNumber() / params.getProductInstanceForHour();
        if (params.getTrainingHourPrice().compareTo(normalizedProductHourPrice) > 0) {
            trainingHourDifferenceCost = new BigDecimal(params.getUsedProductsNumber() / params.getProductInstanceForHour())
                    .multiply(params.getTrainingHourPrice().subtract(normalizedProductHourPrice));
        }
        BigDecimal hoursPaidWithCashCost = new BigDecimal(hoursPaidWithCash).multiply(params.getTrainingHourPrice());
        elctRmbsHeadDto.setIndSxoAmountDueTotal(trainingHourDifferenceCost.add(hoursPaidWithCashCost));
    }

    private void calculateIndAmountForExam(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        elctRmbsHeadDto.setIndSxoAmountDueTotal(params.getTrainingPrice().subtract(elctRmbsHeadDto.getSxoIndAmountDueTotal()));
    }

    private void setTrainingInstanceWithAppropiateStatus(Long trainingInstanceId, Ereimbursement ereimbursement) {
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
        ereimbursement.setTrainingInstance(trainingInstance);
    }
}
