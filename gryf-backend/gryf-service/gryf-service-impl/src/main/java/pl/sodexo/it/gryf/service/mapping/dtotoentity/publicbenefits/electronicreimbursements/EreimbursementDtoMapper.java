package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ElctRmbsHeadDto na encję Ereimbursement
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class EreimbursementDtoMapper extends VersionableDtoMapper<ElctRmbsHeadDto, Ereimbursement> {

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private EreimbursementStatusRepository ereimbursementStatusRepository;

    @Autowired
    private EreimbursementTypeRepository ereimbursementTypeRepository;

    @Autowired
    private PbeProductInstancePoolRepository pbeProductInstancePoolRepository;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Override
    protected Ereimbursement initDestination() {
        return new Ereimbursement();
    }

    @Override
    protected void map(ElctRmbsHeadDto dto, Ereimbursement entity) {

        //w ElctRmbsHeadDto nie są ustawione kolumny auditable
        setAuditable(dto);

        super.map(dto, entity);
        entity.setId(dto.getErmbsId());
        entity.setEreimbursementType(dto.getTypeCode() != null ? ereimbursementTypeRepository.get(dto.getTypeCode()) : null);
        entity.setTrainingInstance(dto.getTrainingInstanceId() != null ? trainingInstanceRepository.get(dto.getTrainingInstanceId()) : null);
        entity.setProductInstancePool(dto.getPoolId() != null ? pbeProductInstancePoolRepository.get(dto.getPoolId()) : null);
        entity.setEreimbursementStatus(dto.getStatusCode() != null ? ereimbursementStatusRepository.get(dto.getStatusCode()) : null);
        entity.setSxoTiAmountDueTotal(dto.getSxoTiAmountDueTotal());
        entity.setSxoIndAmountDueTotal(dto.getSxoIndAmountDueTotal());
        entity.setIndTiAmountDueTotal(dto.getIndTiAmountDueTotal());
        entity.setIndOwnContributionUsed(dto.getIndOwnContributionUsed());
        entity.setIndSubsidyValue(dto.getIndSubsidyValue());
        entity.setTiReimbAccountNumber(dto.getTiReimbAccountNumber());
        entity.setRequiredCorrectionDate(dto.getRequiredCorrectionDate());
        entity.setReimbursementDate(dto.getReimbursementDate());
        entity.setExpiredProductsNum(dto.getExpiredProductsNum());
        entity.setRejectionReasonId(dto.getRejectionReasonId());
        entity.setRejectionDetails(dto.getRejectionDetails());
    }

    private void setAuditable(ElctRmbsHeadDto dto){
        if(dto.getErmbsId() != null){
            Auditable auditable = ereimbursementRepository.getAuditableInfoById(dto.getErmbsId());
            dto.setCreatedUser(auditable.getCreatedUser());
            dto.setCreatedTimestamp(auditable.getCreatedTimestamp());
            dto.setModifiedUser(auditable.getModifiedUser());
            dto.setModifiedTimestamp(auditable.getModifiedTimestamp());
        }
    }

}
