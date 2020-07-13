package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsLineDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementLine;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Mapper mapujący encję na dto
 *
 * Created by dptaszynski on 2020-07-06.
 */
@Component
public class EreimbursementLineEntityMapper extends VersionableEntityMapper<EreimbursementLine, ElctRmbsLineDto> {

    @Override
    protected ElctRmbsLineDto initDestination() {
        return new ElctRmbsLineDto();
    }

    @Override
    public void map(EreimbursementLine entity, ElctRmbsLineDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setEreimbursementId(entity.getEreimbursement().getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setUsedProductsNumber(entity.getUsedProductsNumber());
        dto.setOwnContributionPercentage(entity.getOwnContributionPercentage());
        dto.setIndSubsidyPercentage(entity.getIndSubsidyPercentage());
        dto.setSxoTiAmountDueTotal(entity.getSxoTiAmountDueTotal());
        dto.setSxoIndAmountDueTotal(entity.getSxoIndAmountDueTotal());
        dto.setIndOwnContributionUsed(entity.getIndOwnContributionUsed());
        dto.setIndSubsidyValue(entity.getIndSubsidyValue());
    }

}
