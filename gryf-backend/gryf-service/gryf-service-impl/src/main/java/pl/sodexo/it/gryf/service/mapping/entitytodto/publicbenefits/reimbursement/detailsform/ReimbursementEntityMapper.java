package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRequired;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.searchform.EnterpriseEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.searchform.ReimbursementDeliveryEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.utils.ReimbursementCalculationHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementEntityMapper extends VersionableEntityMapper<Reimbursement, ReimbursementDTO> {

    @Autowired
    private EnterpriseEntityToSearchResultMapper enterpriseEntityToSearchResultMapper;

    @Autowired
    private ReimbursementDeliveryEntityToSearchResultMapper reimbursementDeliveryEntityToSearchResultMapper;

    @Autowired
    private ReimbursementTrainingEntityMapper reimbursementTrainingEntityMapper;

    @Autowired
    private ReimbursementAttachmentEntityMapper reimbursementAttachmentEntityMapper;

    @Autowired
    private ReimbursementTraineeAttachmentRequiredEntityMapper reimbursementTraineeAttachmentRequiredEntityMapper;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ReimbursementDTO initDestination() {
        return new ReimbursementDTO();
    }

    @Override
    public void map(Reimbursement entity, ReimbursementDTO dto) {
        super.map(entity, dto);
        ReimbursementDelivery delivery = entity.getReimbursementDelivery();
        ReimbursementPattern pattern = delivery != null ? delivery.getReimbursementPattern() : null;

        dto.setId(entity.getId());
        dto.setReimbursementDate(entity.getReimbursementDate());
        dto.setAnnouncementDate(entity.getAnnouncementDate());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setInvoiceAnonGrossAmount(entity.getInvoiceAnonGrossAmount());
        dto.setInvoiceAnonVouchAmount(entity.getInvoiceAnonVouchAmount());
        dto.setTrainingInstitutionReimbursementAccountNumber(entity.getTrainingInstitutionReimbursementAccountNumber());
        dto.setEnterprise(enterpriseEntityToSearchResultMapper.convert(entity.getEnterprise()));
        dto.setCreatedUser(entity.getCreatedUser());
        dto.setSxoTiAmountDueTotal(entity.getSxoTiAmountDueTotal());
        dto.setSxoEntAmountDueTotal(entity.getSxoEntAmountDueTotal());
        dto.setTransferDate(entity.getTransferDate());
        dto.setRemarks(entity.getRemarks());

        dto.setRequiredCorrectionDate(entity.getRequiredCorrectionDate());
        dto.setCorrectionDate(entity.getCorrectionDate());
        dto.setCorrectionsNumber(entity.getCorrectionsNumber());
        dto.setCorrectionReason(entity.getCorrectionReason());

        dto.setReimbursementDelivery(reimbursementDeliveryEntityToSearchResultMapper.convert(entity.getReimbursementDelivery()));
        dto.setReimbursementTrainings(reimbursementTrainingEntityMapper.convert(entity.getReimbursementTrainings()));
        dto.setReimbursementAttachments(reimbursementAttachmentEntityMapper.convert(entity.getReimbursementAttachments()));
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));

        dto.setReimbursementTraineeAttachmentRequiredList(reimbursementTraineeAttachmentRequiredEntityMapper.convert(pattern != null ?
                pattern.getReimbursementTraineeAttachmentRequiredList() : new ArrayList<ReimbursementTraineeAttachmentRequired>()));

        dto.setEntToTiAmountDueTotal(ReimbursementCalculationHelper.calculateEntToTiAmountDueTotal(dto));
        dto.setUsedOwnEntContributionAmountTotal(ReimbursementCalculationHelper.calculateUsedOwnEntContributionAmountTotal(dto));
        dto.setGrantAmountTotal(ReimbursementCalculationHelper.calculateGrantAmountTotal(dto));
        dto.setGrantAmountPayedToTiTotal(ReimbursementCalculationHelper.calculateGrantAmountPayedToTiTotal(dto));
        dto.setTrainingCostTotal(ReimbursementCalculationHelper.calculateTrainingCostTotal(dto));

        dto.setVersion(entity.getVersion());
    }

    //TODO AdamK: chwilowo tak, po przeniesieniu dto do commonsów wrócę do tematu
    public void map(Reimbursement entity, ReimbursementDTO dto, Date reimbursementDate) {
        map(entity ,dto);
        dto.setReimbursementDate(reimbursementDate);
    }

    public ReimbursementDTO convert(Reimbursement source, Date reimbursementDate) {
        ReimbursementDTO destination = initDestination();
        if (source != null) {
            map(source, destination, reimbursementDate);
        }
        return destination;
    }

}
