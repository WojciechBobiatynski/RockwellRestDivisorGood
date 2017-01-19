package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.common.exception.NoAppropriateData;
import pl.sodexo.it.gryf.common.exception.NoCalculationParamsException;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementInvoiceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.dao.GrantProgramSearchDao;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.model.api.FinanceNoteResult;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionAttachmentService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementDtoMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.CorrectionValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements.ErmbsValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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
    private EreimbursementTypeRepository ereimbursementTypeRepository;

    @Autowired
    private GrantProgramSearchDao grantProgramSearchDao;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CorrectionAttachmentService correctionAttachmentService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailDtoCreator mailDtoCreator;

    @Autowired
    private EreimbursementInvoiceRepository ereimbursementInvoiceRepository;

    @Autowired
    private PbeProductInstancePoolLocalService pbeProductInstancePoolLocalService;

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

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
    @Cacheable(cacheName = "elctRmbsTypes")
    public List<SimpleDictionaryDto> findElctRmbsTypes() {
        return electronicReimbursementsDao.findElctRmbsTypes();
    }

    @Override
    public ElctRmbsHeadDto createRmbsDtoByTrainingInstanceId(Long trainingInstanceId) {
        ElctRmbsHeadDto elctRmbsHeadDto = electronicReimbursementsDao.findEcltRmbsByTrainingInstanceId(trainingInstanceId);
        if (elctRmbsHeadDto != null) {
            return elctRmbsHeadDto;
        }
        ermbsValidator.validateBeforeCreateForTrainingInstance(trainingInstanceId);
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
        ermbsValidator.isCorrectStatusTransition(elctRmbsHeadDto.getErmbsId(), EreimbursementStatus.NEW_ERMBS);
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
        ermbsValidator.isCorrectStatusTransition(elctRmbsHeadDto.getErmbsId(), EreimbursementStatus.TO_ERMBS);
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
        ermbsValidator.isCorrectStatusTransition(elctRmbsHeadDto.getErmbsId(), EreimbursementStatus.TO_CORRECT);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        List<CorrectionAttachmentDto> correctionAttachmentDtoList = correctionAttachmentService.createCorrAttIfNotExistsForErmbsAttBeingChanged(elctRmbsHeadDto);
        ermbsAttachmentService.manageErmbsAttachmentsForCorrection(elctRmbsHeadDto, ErmbsAttachmentStatus.TEMP);
        correctionAttachmentService.saveCorrectionAttachments(correctionAttachmentDtoList);
        return ereimbursement.getId();
    }

    @Override
    public Long sendToReimburseWithCorr(ElctRmbsHeadDto elctRmbsHeadDto) {
        ermbsValidator.validateRmbs(elctRmbsHeadDto);
        ermbsValidator.isCorrectStatusTransition(elctRmbsHeadDto.getErmbsId(), EreimbursementStatus.TO_ERMBS);
        calculateCharges(elctRmbsHeadDto);
        Ereimbursement ereimbursement = saveErmbsData(elctRmbsHeadDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ereimbursement.setArrivalDate(new Date());
        ereimbursement.setReimbursementDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), applicationParameters.getBusinessDaysNumberForReimbursement()));
        List<CorrectionAttachmentDto> correctionAttachmentDtoList = correctionAttachmentService.createCorrAttIfNotExistsForErmbsAttBeingChanged(elctRmbsHeadDto);
        ermbsAttachmentService.manageErmbsAttachmentsForCorrection(elctRmbsHeadDto, ErmbsAttachmentStatus.SENDED);
        correctionAttachmentService.saveCorrectionAttachments(correctionAttachmentDtoList);
        correctionService.completeCorrection(elctRmbsHeadDto.getLastCorrectionDto().getId());
        return ereimbursement.getId();
    }

    @Override
    public Long sendToCorrect(CorrectionDto correctionDto) {
        ermbsValidator.isCorrectStatusTransition(correctionDto.getErmbsId(), EreimbursementStatus.TO_CORRECT);
        Ereimbursement ereimbursement = ereimbursementRepository.get(correctionDto.getErmbsId());
        ermbsValidator.validateToCorrection(ereimbursement);
        correctionValidator.validateCorrection(correctionDto);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_CORRECT));
        ereimbursement = ereimbursementRepository.save(ereimbursement);
        correctionService.createAndSaveCorrection(correctionDto);
        sendMailsToTiUsers(ereimbursement.getId());
        return ereimbursement.getId();
    }

    @Override
    public Long createDocuments(Long rmbsId) {
        ermbsValidator.isCorrectStatusTransition(rmbsId, EreimbursementStatus.GENERATED_DOCUMENTS);
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.GENERATED_DOCUMENTS));
        ereimbursementRepository.update(ereimbursement, ereimbursement.getId());

        gryfPLSQLRepository.flush();
        FinanceNoteResult financeNoteResult = gryfPLSQLRepository.createCreditNoteForReimbursment(rmbsId);

        EreimbursementInvoice ereimbursementInvoice = new EreimbursementInvoice();
        ereimbursementInvoice.setEreimbursement(ereimbursement);
        ereimbursementInvoice.setInvoiceId(financeNoteResult.getInvoiceId());
        ereimbursementInvoice.setInvoiceNumber(financeNoteResult.getInvoiceNumber());
        ereimbursementInvoice.setInvoiceType(financeNoteResult.getInvoiceType());
        ereimbursementInvoice.setInvoiceDate(financeNoteResult.getInvoiceDate());
        ereimbursementInvoiceRepository.save(ereimbursementInvoice);
        ereimbursement.getEreimbursementInvoices().add(ereimbursementInvoice);

        return ereimbursement.getId();
    }

    @Override
    public Long printReports(Long rmbsId) {
        ermbsValidator.isCorrectStatusTransition(rmbsId, EreimbursementStatus.TO_VERIFY);
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);
        List<EreimbursementReport> ereimbursementReports = new ArrayList<>();

        createCreditNote(ereimbursement, ereimbursementReports);
        createBankTransferConfirmation(ereimbursement, ereimbursementReports);
        createGrantAidConfirmation(ereimbursement, ereimbursementReports);

        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_VERIFY));
        ereimbursement.setEreimbursementReports(ereimbursementReports);
        ereimbursementRepository.update(ereimbursement, ereimbursement.getId());
        return ereimbursement.getId();
    }

    private void createGrantAidConfirmation(Ereimbursement ereimbursement, List<EreimbursementReport> ereimbursementReports) {
        Boolean isMsp = electronicReimbursementsDao.isErmbsForEnterprise(ereimbursement.getId());
        if(isMsp == null){
            throw new NoAppropriateData("Brak odpowiednich danych. Nie można stwierdzić czy MSP czy IND");
        }
        if(isMsp) {
            String grantAidConfirmationLocation = reportService.generateGrantAidConfirmationForReimbursment(ereimbursement.getId());
            EreimbursementReport report = new EreimbursementReport();
            report.setEreimbursement(ereimbursement);
            report.setFileLocation(grantAidConfirmationLocation);
            report.setTypeName(ReportTemplateCode.GRANT_AID_CONFIRMATION.getTypeName());
            ereimbursementReports.add(report);
        }
    }

    private void createBankTransferConfirmation(Ereimbursement ereimbursement, List<EreimbursementReport> ereimbursementReports) {
        String bankTransferConfirmationLocation = reportService.generateBankTransferConfirmationForReimbursment(ereimbursement.getId());
        EreimbursementReport report = new EreimbursementReport();
        report.setEreimbursement(ereimbursement);
        report.setFileLocation(bankTransferConfirmationLocation);
        report.setTypeName(ReportTemplateCode.BANK_TRANSFER_CONFIRMATION.getTypeName());
        ereimbursementReports.add(report);
    }

    private void createCreditNote(Ereimbursement ereimbursement, List<EreimbursementReport> ereimbursementReports) {
        EreimbursementInvoice ereimbursementInvoice = getEreimbursementInvoice(ereimbursement);
        String creditNoteLocation = reportService.generateCreditNoteForReimbursment(ereimbursement.getId(), ereimbursementInvoice.getInvoiceNumber());
        EreimbursementReport report = new EreimbursementReport();
        report.setEreimbursement(ereimbursement);
        report.setFileLocation(creditNoteLocation);
        report.setTypeName(ReportTemplateCode.CREDIT_NOTE.getTypeName());
        ereimbursementReports.add(report);
    }

    @Override
    public Long cancel(Long rmbsId) {
        ermbsValidator.isCorrectStatusTransition(rmbsId, EreimbursementStatus.CANCELED);
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.CANCELED));
        setDoneReimburseStatusForTiInstance(ereimbursement);
        ereimbursementRepository.update(ereimbursement, ereimbursement.getId());
        return ereimbursement.getId();
    }

    @Override
    public Long confirm(Long rmbsId) {
        ermbsValidator.isCorrectStatusTransition(rmbsId, EreimbursementStatus.SETTLED);
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);
        ereimbursement.setReconDate(new Date());
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.SETTLED));
        setReimbReimburseStatusForTiInstance(ereimbursement);
        pbeProductInstancePoolLocalService.reimbursPools(ereimbursement);
        ereimbursementRepository.update(ereimbursement, ereimbursement.getId());
        return ereimbursement.getId();
    }

    @Override
    public Long expire(Long rmbsId) {
        Ereimbursement ereimbursement = ereimbursementRepository.get(rmbsId);
        //TODO: akmiecinski pozminiac statusy

        pbeProductInstancePoolLocalService.expirePools(ereimbursement);
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.SETTLED));
        ereimbursementRepository.update(ereimbursement, ereimbursement.getId());
        return ereimbursement.getId();
    }

    private void sendMailsToTiUsers(Long ermbsId) {
        CorrectionNotificationEmailParamsDto corrNotifParamsByErmbsId = correctionService.findCorrNotifParamsByErmbsId(ermbsId);
        List<MailDTO> mailsToSend = mailDtoCreator.createMailDTOsForCorrecionNotification(corrNotifParamsByErmbsId);
        mailsToSend.stream().forEach(mailDTO -> mailService.scheduleMail(mailDTO));
    }

    private Ereimbursement saveErmbsData(ElctRmbsHeadDto elctRmbsHeadDto) {
        Ereimbursement ereimbursement = ereimbursementDtoMapper.convert(elctRmbsHeadDto);
        ereimbursement = ereimbursement.getId() != null ? ereimbursementRepository.update(ereimbursement, ereimbursement.getId()) : ereimbursementRepository.save(ereimbursement);
        return ereimbursement;
    }

    private void setErmbsDataWhenSave(Ereimbursement ereimbursement) {
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.NEW_ERMBS));
        ereimbursement.setEreimbursementType(ereimbursementTypeRepository.get(EreimbursementType.TI_INST));
        setToReimburseStatusForTiInstance(ereimbursement);
    }

    private void setErmbsDataWhenSendToReimburse(Ereimbursement ereimbursement) {
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
        ereimbursement.setEreimbursementType(ereimbursementTypeRepository.get(EreimbursementType.TI_INST));
        setToReimburseStatusForTiInstance(ereimbursement);
        ereimbursement.setArrivalDate(new Date());
        ereimbursement.setReimbursementDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), applicationParameters.getBusinessDaysNumberForReimbursement()));
    }

    private void setToReimburseStatusForTiInstance(Ereimbursement ereimbursement) {
        ereimbursement.getTrainingInstance().setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
    }

    private void setDoneReimburseStatusForTiInstance(Ereimbursement ereimbursement) {
        ereimbursement.getTrainingInstance().setStatus(trainingInstanceStatusRepository.get(GryfConstants.DONE_TRAINING_INSTANCE_STATUS_CODE));
    }

    private void setReimbReimburseStatusForTiInstance(Ereimbursement ereimbursement) {
        ereimbursement.getTrainingInstance().setStatus(trainingInstanceStatusRepository.get(GryfConstants.REIMBURSED_TRAINING_INSTANCE_STATUS_CODE));
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

    private EreimbursementInvoice getEreimbursementInvoice(Ereimbursement ereimbursement){
        List<EreimbursementInvoice> ereimbursementInvoices = ereimbursement.getEreimbursementInvoices();
        if(ereimbursementInvoices.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Rozliczenie [%s] nie zawiera żadnej noty.", ereimbursement.getId()));
        }
        if(ereimbursementInvoices.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Rozliczenie [%s] zawiera wiecej niż jedną notę.", ereimbursement.getId()));
        }
        return ereimbursementInvoices.get(0);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void createReimbursementForExpiredInstancesPool() {
        List<PbeProductInstancePoolDto> expiredPoolInstances = pbeProductInstancePoolLocalService.findExpiredPoolInstances();
        Consumer<PbeProductInstancePoolDto> pInsPoolCons = pbeProductInstancePool -> {
            Ereimbursement ereimbursement = new Ereimbursement();
            ereimbursement.setEreimbursementType(ereimbursementTypeRepository.get(EreimbursementType.URSVD_POOL));
            ereimbursement.setProductInstancePool(productInstancePoolRepository.get(pbeProductInstancePool.getId()));
            ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(EreimbursementStatus.TO_ERMBS));
            //TODO: pobrać procent wkładu własnego z parametrów
            BigDecimal ownContributionPercentage = new BigDecimal("0.13");
            ereimbursement.setSxoIndAmountDueTotal(
                    BigDecimal.valueOf(pbeProductInstancePool.getAvailableNum()).multiply(pbeProductInstancePool.getProductValue()).multiply(ownContributionPercentage).setScale(2, RoundingMode.HALF_UP));
            ereimbursementRepository.save(ereimbursement);
        };
        expiredPoolInstances.stream().forEach(pInsPoolCons);
    }

    @Override
    public UnrsvPoolRmbsDto findUnrsvPoolRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findUnrsvPoolRmbsById(ermbsId);
    }

    @Override
    public boolean isEreimbursementInLoggedUserInstitution(Long ereimbursementId){
        return ereimbursementRepository.isInLoggedUserInstitution(ereimbursementId, GryfUser.getLoggedUserLogin());
    }
}
