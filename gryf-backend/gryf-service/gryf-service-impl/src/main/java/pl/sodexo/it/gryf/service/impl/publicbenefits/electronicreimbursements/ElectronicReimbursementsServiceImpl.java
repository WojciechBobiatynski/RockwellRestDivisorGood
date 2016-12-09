package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
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
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionAttachmentService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements.EreimbursementEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.CorrectionValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.ErmbsValidator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ReportService reportService;

    @Autowired
    private CorrectionAttachmentService correctionAttachmentService;

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
        //wyliczanie składek zawsze przed zapisem na wypadek manipulowania danymi z frontu
        calculateCharges(elctRmbsHeadDto);
        //musimy zapisać rmbs, żeby mieć ID potrzebne do nadania odpowiedniej nazwy załącznikom
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        elctRmbsHeadDto.setErmbsId(ereimbursement.getId());
        ermbsAttachmentService.manageErmbsAttachments(elctRmbsHeadDto, ErmbsAttachmentStatus.TEMP);
        setErmbsDataWhenSave(ereimbursement);
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburse(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        //wyliczanie składek zawsze przed zapisem na wypadek manipulowania danymi z frontu
        calculateCharges(elctRmbsHeadDto);
        //musimy zapisać rmbs, żeby mieć ID potrzebne do nadania odpowiedniej nazwy załącznikom
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        elctRmbsHeadDto.setErmbsId(ereimbursement.getId());
        ermbsAttachmentService.manageErmbsAttachments(elctRmbsHeadDto, ErmbsAttachmentStatus.SENDED);
        setErmbsDataWhenSendToReimburse(ereimbursement);
        return ereimbursement.getId();
    }

    @Override
    public Long saveErmbsWithCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        List<CorrectionAttachmentDto> correctionAttachmentDtoList = correctionAttachmentService.createCorrAttIfNotExistsForErmbsAttBeingChanged(elctRmbsHeadDto);
        ermbsAttachmentService.manageErmbsAttachmentsForCorrection(elctRmbsHeadDto, ErmbsAttachmentStatus.TEMP);
        correctionAttachmentService.saveCorrectionAttachments(correctionAttachmentDtoList);
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburseWithCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        calculateCharges(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        List<CorrectionAttachmentDto> correctionAttachmentDtoList = correctionAttachmentService.createCorrAttIfNotExistsForErmbsAttBeingChanged(elctRmbsHeadDto);
        ermbsAttachmentService.manageErmbsAttachmentsForCorrection(elctRmbsHeadDto, ErmbsAttachmentStatus.SENDED);
        correctionAttachmentService.saveCorrectionAttachments(correctionAttachmentDtoList);
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

    private Ereimbursement saveErmbsData(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
        ereimbursement = ereimbursement.getId() != null ? ereimbursementRepository.update(ereimbursement, ereimbursement.getId()) : ereimbursementRepository.save(ereimbursement);
        return ereimbursement;
    }

    private void setErmbsDataWhenSave(Ereimbursement ereimbursement) {
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.NEW_ERMBS));
        setToReimburseStatusForTiInstance(ereimbursement);
    }

    private void setErmbsDataWhenSendToReimburse(Ereimbursement ereimbursement) {
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ereimbursement.setArrivalDate(new Date());
        ereimbursement.setReimbursementDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), applicationParameters.getBusinessDaysNumberForReimbursement()));
        setToReimburseStatusForTiInstance(ereimbursement);
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

    @Override
    public void createDocuments(Long rmbsId) {
        //GENERATOE REPORT
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rmbsId", rmbsId);
        parameters.put("companyName", applicationParameters.getSodexoName());
        parameters.put("companyAddress1", applicationParameters.getSodexoAddress1());
        parameters.put("companyAddress2", applicationParameters.getSodexoAddress2());
        parameters.put("companyVatRegNum", applicationParameters.getSodexoVatRegNum());
        parameters.put("companyBankName", applicationParameters.getSodexoBankName());
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);

        //TODO: tbilski nazwa pliku
        String reportFileName = String.format("%s_Nota_uznaniowa.pdf", rmbsId);
        String reportLocation = reportService.generateReport(ReportTemplateCode.CREDIT_NOTE, reportFileName,
                FileType.E_REIMBURSEMENTS, parameters, ReportSourceType.EREIMBURSMENT, rmbsId);

    }
}
