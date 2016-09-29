package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.service.utils.ReimbursementCalculationHelper;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTrainingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementTrainingEntityMapper extends GryfEntityMapper<ReimbursementTraining, ReimbursementTrainingDTO> {

    @Autowired
    private ReimbursementTraineeEntityMapper reimbursementTraineeEntityMapper;

    @Override
    protected ReimbursementTrainingDTO initDestination() {
        return new ReimbursementTrainingDTO();
    }

    @Override
    public void map(ReimbursementTraining entity, ReimbursementTrainingDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setTrainingName(entity.getTrainingName());
        dto.setTrainingDateFrom(entity.getTrainingDateFrom());
        dto.setTrainingDateTo(entity.getTrainingDateTo());
        dto.setTrainingPlace(entity.getTrainingPlace());
        dto.setGrantOwnerAidProductId((entity.getGrantOwnerAidProduct() != null) ? entity.getGrantOwnerAidProduct().getId() : null);
        dto.setProductsNumber(entity.getProductsNumber());
        dto.setTrainingHourGrossPrice(entity.getTrainingHourGrossPrice());
        dto.setTrainingHoursTotal(entity.getTrainingHoursTotal());
        dto.setProductTotalValue(entity.getProductTotalValue());
        dto.setProductAidValue(entity.getProductAidValue());

        dto.setVoucherRefundedTrainingHourValue(ReimbursementCalculationHelper.calculateVoucherRefundedTrainingHourValue(entity));
        dto.setSxoTiAmountDue(entity.getSxoTiAmountDue());
        dto.setEntToTiAmountDue(ReimbursementCalculationHelper.calculateEntToTiAmountDue(entity, dto.getSxoTiAmountDue()));
        dto.setSxoEntAmountDue(entity.getSxoEntAmountDue());
        dto.setUsedOwnEntContributionAmount(ReimbursementCalculationHelper.calculateUsedOwnEntContributionAmount(entity));
        dto.setGrantAmount(ReimbursementCalculationHelper.calculateGrantAmount(entity));
        dto.setGrantAmountPayedToTi(ReimbursementCalculationHelper.calculateGrantAmountPayedToTi(dto.getEntToTiAmountDue(), dto.getUsedOwnEntContributionAmount()));
        dto.setTrainingCost(ReimbursementCalculationHelper.calculateTrainingCost(entity));

        dto.setReimbursementTrainees(reimbursementTraineeEntityMapper.convert(entity.getReimbursementTrainees()));
    }

}
