package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Mapper mapujący encję na dto
 *
 * Created by akmiecinski on 2016-11-28.
 */
@Component
public class EreimbursementEntityMapper extends VersionableEntityMapper<Ereimbursement, ElctRmbsHeadDto> {

    @Autowired
    private ErmbsAttachmentEntityMapper ermbsAttachmentEntityMapper;

    @Autowired
    private EreimbursementLineEntityMapper ereimbursementLineEntityMapper;

    @Override
    protected ElctRmbsHeadDto initDestination() {
        return new ElctRmbsHeadDto();
    }

    @Override
    public void map(Ereimbursement entity, ElctRmbsHeadDto dto) {
        super.map(entity, dto);
        dto.setErmbsId(entity.getId());
        dto.setTypeCode(entity.getEreimbursementType().getCode());
        dto.setTrainingInstanceId(entity.getTrainingInstance() != null ? entity.getTrainingInstance().getId() : null);
        dto.setPoolId(entity.getProductInstancePool() != null ? entity.getProductInstancePool().getId() : null);
        dto.setStatusCode(entity.getEreimbursementStatus() != null ? entity.getEreimbursementStatus().getId() : null);
        dto.setSxoTiAmountDueTotal(entity.getSxoTiAmountDueTotal());
        dto.setSxoIndAmountDueTotal(entity.getSxoIndAmountDueTotal());
        dto.setIndTiAmountDueTotal(entity.getIndTiAmountDueTotal());
        dto.setIndOwnContributionUsed(entity.getIndOwnContributionUsed());
        dto.setIndSubsidyValue(entity.getIndSubsidyValue());
        dto.setTiReimbAccountNumber(entity.getTiReimbAccountNumber());
        dto.setRequiredCorrectionDate(entity.getRequiredCorrectionDate());
        dto.setReimbursementDate(entity.getReimbursementDate());
        dto.setExpiredProductsNum(entity.getExpiredProductsNum());
        dto.setRejectionReasonId(entity.getRejectionReasonId());
        dto.setRejectionDetails(entity.getRejectionDetails());
        if (entity.getErmbsAttachmentList() != null && !entity.getErmbsAttachmentList().isEmpty()) {
            dto.setAttachments(ermbsAttachmentEntityMapper.convert(entity.getErmbsAttachmentList()));
        }
        if (entity.getEreimbursementLines() != null && !entity.getEreimbursementLines().isEmpty()) {
            dto.setElctRmbsLineDtos(ereimbursementLineEntityMapper.convert(entity.getEreimbursementLines()));
        }
    }

}
