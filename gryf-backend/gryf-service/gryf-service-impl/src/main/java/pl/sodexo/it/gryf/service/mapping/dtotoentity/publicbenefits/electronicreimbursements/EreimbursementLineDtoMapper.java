package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsLineDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementLineRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementLine;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ElctRmbsLineDto na encję EreimbursementLine
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class EreimbursementLineDtoMapper extends VersionableDtoMapper<ElctRmbsLineDto, EreimbursementLine> {

    @Autowired
    private EreimbursementLineRepository ereimbursementLineRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Override
    protected EreimbursementLine initDestination() {
        return new EreimbursementLine();
    }

    @Override
    protected void map(ElctRmbsLineDto dto, EreimbursementLine entity) {

        //w ElctRmbsLineDto nie są ustawione kolumny auditable
        setAuditable(dto);

        super.map(dto, entity);
        entity.setId(dto.getEreimbursementId());
        entity.setEreimbursement(dto.getEreimbursementId() != null ? ereimbursementRepository.get(dto.getEreimbursementId()) : null);
        entity.setOrder(dto.getOrderId() != null ? orderRepository.get(dto.getOrderId()) : null);
        entity.setUsedProductsNumber(dto.getUsedProductsNumber());
        entity.setOwnContributionPercentage(dto.getOwnContributionPercentage());
        entity.setIndSubsidyPercentage(dto.getIndSubsidyPercentage());
        entity.setSxoTiAmountDueTotal(dto.getSxoTiAmountDueTotal());
        entity.setSxoIndAmountDueTotal(dto.getSxoIndAmountDueTotal());
        entity.setIndOwnContributionUsed(dto.getIndOwnContributionUsed());
        entity.setIndSubsidyValue(dto.getIndSubsidyValue());
    }

    private void setAuditable(ElctRmbsLineDto dto){
        if(dto.getId() != null){
            Auditable auditable = ereimbursementLineRepository.getAuditableInfoById(dto.getId());
            dto.setCreatedUser(auditable.getCreatedUser());
            dto.setCreatedTimestamp(auditable.getCreatedTimestamp());
            dto.setModifiedUser(auditable.getModifiedUser());
            dto.setModifiedTimestamp(auditable.getModifiedTimestamp());
        }
    }

}
