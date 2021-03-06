package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.searchform.ReimbursementDeliveryEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionEntityToSearchResultMapper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementDeliveryEntityMapper extends VersionableEntityMapper<ReimbursementDelivery, ReimbursementDeliveryDTO> {

    @Autowired
    private TrainingInstitutionEntityToSearchResultMapper trainingInstitutionEntityToSearchResultMapper;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private ReimbursementDeliveryEntityToSearchResultMapper reimbursementDeliveryEntityToSearchResultMapper;

    @Autowired
    private ReimbursementAttachmentRequiredEntityMapper reimbursementAttachmentRequiredEntityMapper;

    @Autowired
    private ReimbursementTraineeAttachmentRequiredEntityMapper reimbursementTraineeAttachmentRequiredEntityMapper;

    @Override
    protected ReimbursementDeliveryDTO initDestination() {
        return new ReimbursementDeliveryDTO();
    }

    @Override
    public void map(ReimbursementDelivery entity, ReimbursementDeliveryDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setReimbursementPattern(dictionaryEntityMapper.convert(entity.getReimbursementPattern()));
        dto.setTrainingInstitution(trainingInstitutionEntityToSearchResultMapper.convert(entity.getTrainingInstitution()));
        dto.setDeliveryAddress(entity.getDeliveryAddress());
        dto.setDeliveryZipCode(entity.getDeliveryZipCode());
        dto.setDeliveryCityName(entity.getDeliveryCityName());
        dto.setPlannedReceiptDate(entity.getPlannedReceiptDate());
        dto.setRequestDate(entity.getRequestDate());
        dto.setDeliveryDate(entity.getDeliveryDate());
        dto.setWaybillNumber(entity.getWaybillNumber());
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));
        dto.setAnnouncementDate(entity.getReimbursementAnnouncementDate());
        dto.setParentId(entity.getMasterReimbursementDelivery() != null ? entity.getMasterReimbursementDelivery().getId() : null);
        dto.setDeliverySecondary(entity.getMasterReimbursementDelivery() != null);
        dto.setRemarks(entity.getRemarks());
        dto.setVersion(entity.getVersion());
    }

    //TODO AdamK: chwilowo tak, po przeniesieniu dto do commons??w wr??c?? do tematu
    public ReimbursementDTO initialConvert(ReimbursementDelivery source, Date reimbursementDate) {
        ReimbursementDTO destination = new ReimbursementDTO();
        if (source != null) {
            map(source, destination, reimbursementDate);
        }
        return destination;
    }

    public void map(ReimbursementDelivery entity, ReimbursementDTO dto, Date reimbursementDate) {
        ReimbursementPattern pattern = entity.getReimbursementPattern();

        dto.setReimbursementDelivery(reimbursementDeliveryEntityToSearchResultMapper.convert(entity));
        dto.setAnnouncementDate(new Date());
        dto.setReimbursementDate(reimbursementDate);
        dto.setReimbursementAttachments(reimbursementAttachmentRequiredEntityMapper.convert(pattern.getReimbursementAttachmentRequiredList()));
        dto.setReimbursementTraineeAttachmentRequiredList(reimbursementTraineeAttachmentRequiredEntityMapper.convert(pattern != null ?
                pattern.getReimbursementTraineeAttachmentRequiredList() : new ArrayList<>()));
    }

}
