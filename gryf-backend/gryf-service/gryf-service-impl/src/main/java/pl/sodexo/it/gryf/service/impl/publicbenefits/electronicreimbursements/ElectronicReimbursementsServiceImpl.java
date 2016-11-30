package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.AttachmentParentType;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.exception.NoCalculationParamsException;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementStatus;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsAttachmentDtoMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.ErmbsValidator;

import java.math.BigDecimal;
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
    private EreimbursementAttachmentRepository ereimbursementAttachmentRepository;

    @Autowired
    private ErmbsAttachmentDtoMapper ermbsAttachmentDtoMapper;

    @Autowired
    private ProductSearchDao productSearchDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private EreimbursementDtoMapper ereimbursementDtoMapper;

    @Autowired
    private ErmbsValidator ermbsValidator;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private CorrectionService correctionService;

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
        calculateCharges(elctRmbsHeadDto, trainingInstanceId);
        elctRmbsHeadDto.setProducts(productSearchDao.findProductsByTrainingInstanceId(trainingInstanceId));
        return elctRmbsHeadDto;
    }

    @Override
    public ElctRmbsHeadDto findEcltRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findEcltRmbsById(ermbsId);
    }

    @Override
    public Long saveErmbs(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.NEW_ERMBS));
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburse(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ereimbursement.setReimbursementDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), applicationParameters.getBusinessDaysNumberForReimbursement()));
        return ereimbursement.getId();
    }

    @Override
    public Integer toCorrect(Long ermbsId) {
        Ereimbursement ereimbursement = ereimbursementRepository.get(ermbsId);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_CORRECT));
        ereimbursement = ereimbursementRepository.save(ereimbursement);
        //TODO: tymczasowa zaślepka, później będzie przekazywany obiekt z guia i zostanie sam zapis
        CorrectionDto correctionDto = new CorrectionDto();
        correctionDto.setErmbsId(ereimbursement.getId());
        correctionDto.setCorrectionReason("ABCD");
        correctionService.createAndSaveCorrection(correctionDto);
        return ereimbursement.getVersion();
    }

    @Override
    public FileDTO getErmbsAttFileById(Long id) {
        ErmbsAttachment attachment = ereimbursementAttachmentRepository.get(id);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getOrginalFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    private Ereimbursement saveErmbsData(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
        setToReimburseStatusForTiInstance(ereimbursement);
        ereimbursement = ereimbursement.getId() != null ? ereimbursementRepository.update(ereimbursement, ereimbursement.getId()) : ereimbursementRepository.save(ereimbursement);
        saveErmbsAttachments(ereimbursement, elctRmbsHeadDto.getAttachments());
        return ereimbursement;
    }

    private void setToReimburseStatusForTiInstance(Ereimbursement ereimbursement) {
        ereimbursement.getTrainingInstance().setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
    }

    private void saveErmbsAttachments(Ereimbursement ereimbursement, List<ErmbsAttachmentDto> ermbsAttachmentsDtoList) {
        for (ErmbsAttachmentDto ermbsAttachment : ermbsAttachmentsDtoList) {
            if (deleteIfMarked(ermbsAttachment))
                continue;
            ErmbsAttachment entity = saveAttachmentEntity(ereimbursement, ermbsAttachment);
            saveFile(ermbsAttachment, entity, ereimbursement);
        }
    }

    private boolean deleteIfMarked(ErmbsAttachmentDto ermbsAttachment) {
        if (ermbsAttachment.isMarkToDelete()) {
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
            ereimbursementAttachmentRepository.delete(entity);
            return true;
        }
        return false;
    }

    private ErmbsAttachment saveAttachmentEntity(Ereimbursement ereimbursement, ErmbsAttachmentDto ermbsAttachment) {
        ErmbsAttachment entity = ermbsAttachmentDtoMapper.convert(ermbsAttachment);
        entity.setEreimbursement(ereimbursement);
        entity = entity.getId() != null ? ereimbursementAttachmentRepository.update(entity, entity.getId()) : ereimbursementAttachmentRepository.save(entity);
        return entity;
    }

    private void saveFile(ErmbsAttachmentDto ermbsAttachment, ErmbsAttachment entity, Ereimbursement ereimbursement) {
        if (ermbsAttachment.isChanged()) {
            fileService.deleteFile(ermbsAttachment.getFileLocation());
            Long trainingInstitutionId = ((GryfTiUser) GryfUser.getLoggedUser()).getTrainingInstitutionId();
            String fileName = String
                    .format("%s_%s_%s_%s_%s", trainingInstitutionId, ereimbursement.getId(), AttachmentParentType.EREIMB, entity.getId(), ermbsAttachment.getFile().getOriginalFilename());
            String newFileName = GryfStringUtils.convertFileName(fileName);
            String filePath = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsAttachment.getFile(), entity);
            entity.setOrginalFileName(ermbsAttachment.getFile().getOriginalFilename());
            entity.setFileLocation(filePath);
        }
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
        elctRmbsHeadDto.setSxoTiAmountDueTotal(sxoAmount);
    }

    private void calculateSxoAmountForExam(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        BigDecimal usedProductsAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(params.getProductValue());
        if (usedProductsAmount.compareTo(params.getTrainingPrice()) > 0) {
            elctRmbsHeadDto.setSxoTiAmountDueTotal(params.getTrainingPrice());
        } else {
            elctRmbsHeadDto.setSxoTiAmountDueTotal(usedProductsAmount);
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
        elctRmbsHeadDto.setIndTiAmountDueTotal(trainingHourDifferenceCost.add(hoursPaidWithCashCost));
    }

    private void calculateIndAmountForExam(ElctRmbsHeadDto elctRmbsHeadDto, CalculationChargesParamsDto params) {
        elctRmbsHeadDto.setIndTiAmountDueTotal(params.getTrainingPrice().subtract(elctRmbsHeadDto.getSxoTiAmountDueTotal()));
    }
}
