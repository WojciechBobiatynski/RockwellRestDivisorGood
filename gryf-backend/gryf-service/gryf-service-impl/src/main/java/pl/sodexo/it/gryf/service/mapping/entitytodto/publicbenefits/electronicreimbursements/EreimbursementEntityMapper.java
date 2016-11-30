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

    @Override
    protected ElctRmbsHeadDto initDestination() {
        return new ElctRmbsHeadDto();
    }

    @Override
    public void map(Ereimbursement entity, ElctRmbsHeadDto dto) {
        super.map(entity, dto);
        dto.setErmbsId(entity.getId());
        dto.setTrainingInstanceId(entity.getTrainingInstance() != null ? entity.getTrainingInstance().getId() : null);
        dto.setStatusCode(entity.getEreimbursementStatus() != null ? entity.getEreimbursementStatus().getId() : null);
        dto.setSxoTiAmountDueTotal(entity.getSxoTiAmountDueTotal());
        dto.setIndTiAmountDueTotal(entity.getIndTiAmountDueTotal());
        dto.setTiReimbAccountNumber(entity.getTiReimbAccountNumber());
        dto.setRequiredCorrectionDate(entity.getRequiredCorrectionDate());
        dto.setReimbursementDate(entity.getReimbursementDate());
        if (entity.getErmbsAttachmentList() != null && !entity.getErmbsAttachmentList().isEmpty()) {
            dto.setAttachments(ermbsAttachmentEntityMapper.convert(entity.getErmbsAttachmentList()));
        }
    }

}
