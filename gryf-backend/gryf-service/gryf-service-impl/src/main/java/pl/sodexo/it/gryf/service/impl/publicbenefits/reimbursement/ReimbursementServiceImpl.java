package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementComplete;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementCorrect;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementService;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementAttachmentLocalService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTrainingService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform.ReimbursementEntityMapper;
import pl.sodexo.it.gryf.service.utils.ReimbursementCalculationHelper;
import pl.sodexo.it.gryf.service.validation.VersionableValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement.ReimbursementValidator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Service
@Transactional
public class ReimbursementServiceImpl implements ReimbursementService {

    //FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementDeliveryRepository reimbursementDeliveryRepository;

    @Autowired
    private ReimbursementAttachmentRepository reimbursementAttachmentRepository;

    @Autowired
    private ReimbursementAttachmentTypeRepository reimbursementAttachmentTypeRepository;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private ReimbursementStatusRepository reimbursementStatusRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private ReimbursementEntityMapper reimbursementEntityMapper;

    @Autowired
    private ReimbursementDeliveryEntityMapper reimbursementDeliveryEntityMapper;

    @Autowired
    private ReimbursementValidator reimbursementValidator;

    @Autowired
    private VersionableValidator versionableValidator;

    @Autowired
    private ReimbursementAttachmentLocalService reimbursementAttachmentLocalService;

    @Autowired
    private ReimbursementTrainingService reimbursementTrainingService;

    //PUBLIC METHODS

    @Override
    public ReimbursementDTO findReimbursement(Long id) {
        Reimbursement reimbursement = reimbursementRepository.get(id);
        return reimbursementEntityMapper.convert(reimbursement);
    }

    @Override
    public ReimbursementDTO createInitialReimbursement(Long reimbursementDeliveryId) {
        ReimbursementDelivery reimbursementDelivery = reimbursementDeliveryRepository.get(reimbursementDeliveryId);
        ReimbursementPattern reimbursementPattern = reimbursementDelivery.getReimbursementPattern();

        String reimbursementDelay = reimbursementPatternService.findReimbursementPatternParam(reimbursementPattern.getId(), ReimbursementPatternParam.REIMBDELAY);
        Date reimbursementDate = gryfPLSQLRepository.getNthBusinessDay(reimbursementDelivery.getReimbursementAnnouncementDate(), Integer.valueOf(reimbursementDelay));

        return reimbursementDeliveryEntityMapper.initialConvert(reimbursementDelivery, reimbursementDate);
    }

    @Override
    public Long saveReimbursement(ReimbursementDTO dto) {

        try{
            Reimbursement entity;

            if(dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.ANNOUNCED_CODE, true);
            }else{
                entity = reimbursementRepository.get(dto.getId());
                reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE,
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
        try {
            Reimbursement entity;

            if (dto.getId() == null) {
                entity = save(dto, ReimbursementStatus.CORRECTED_CODE, true, ValidationGroupReimbursementCorrect.class);
            } else {
                entity = reimbursementRepository.get(dto.getId());
                reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE);
                entity = update(entity, dto, ReimbursementStatus.CORRECTED_CODE, true, ValidationGroupReimbursementCorrect.class);
            }

            correctReimbursement(entity);
            return entity.getId();

            //KASUJEMY PLIKI
        } catch (RuntimeException e) {
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
                reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE);
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
                reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE, ReimbursementStatus.TO_REIMB_CODE);
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
            reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.EXPORTED_CODE);
            entity = update(entity, dto, entity.getStatus().getStatusId(), true, ValidationGroupReimbursementComplete.class);

            //SAVE ATTACHMENT REPORT
            ReimbursementAttachment reportAttachment = new ReimbursementAttachment();
            reportAttachment.setAttachmentType(reimbursementAttachmentTypeRepository.get(ReimbursementAttachmentType.ORIGINAL_CODE));
            reportAttachment.setName("Potwierdzenie realizacji dofinansowania");
            reportAttachment.setOriginalFileName("Potwierdzenie realizacji dofinansowania.pdf");
            entity.addReimbursementAttachment(reportAttachment);
            reimbursementAttachmentRepository.save(reportAttachment);

            //GENERATOE REPORT
            String reportFileName = reimbursementAttachmentLocalService.findAttachmentName(entity, ReimbursementAttachment.ATTACHMENT_TYPE_IN_PATH, reportAttachment.getId(), reportAttachment.getName()) + ".pdf";
            reportLocation = reportService.generateReport(ReportTemplateCode.GRANT_ACKNOWLEDGMENT_REPORT, reportFileName, FileType.REIMBURSEMENTS, ReportSourceType.REIMBURSEMENT, entity.getId());

            //UPDATE REPORT PATH
            reportAttachment.setFileLocation(reportLocation);
            reimbursementAttachmentRepository.update(reportAttachment, reportAttachment.getId());

            return entity.getId();

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            if(!GryfStringUtils.isEmpty(reportLocation)){
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
            reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.EXPORTED_CODE);
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
            reimbursementValidator.validateStatusAccess(entity, ReimbursementStatus.ANNOUNCED_CODE, ReimbursementStatus.CORRECTED_CODE,
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
        reimbursementValidator.validateStatusChange(null, nextStatus);
        if(isValidation) {
            reimbursementValidator.validate(dto, entity, validationClasses);
        }
        //SAVE (NAJPIERW PERSIST ABY MIEC ID DO PLIKOW)
        entity = reimbursementRepository.save(entity);
        fillReimbursement(entity, dto, nextStatus);
        return entity;
    }

    private Reimbursement update(Reimbursement entity, ReimbursementDTO dto, String nextStatus, boolean isValidation, Class ... validationClasses){
        versionableValidator.validateVersion(entity, dto, entity.getId());
        reimbursementValidator.validateStatusChange(entity.getStatus().getStatusId(), nextStatus);
        if(isValidation) {
            reimbursementValidator.validate(dto, entity, validationClasses);
        }
        //UPDATE (NAJPIERW MERGE ABY BYLO ANALOGICZNIE JA W SAVE)
        entity = reimbursementRepository.update(entity, entity.getId());
        fillReimbursement(entity, dto, nextStatus);
        return entity;
    }

    //PRIVATE METHODS - LOGIC

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
                reimbursementTrainingService.organizeTrainingList(entity, dto.getReimbursementTrainings(), true);
                reimbursementAttachmentLocalService.organizeAttachmentList(entity, dto.getReimbursementAttachments(), true);

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
                reimbursementAttachmentLocalService.organizeAttachmentList(entity, dto.getReimbursementAttachments(), false);
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

}
