package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
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

    @Override
    protected Ereimbursement initDestination() {
        return new Ereimbursement();
    }

    @Override
    protected void map(ElctRmbsHeadDto dto, Ereimbursement entity) {
        super.map(dto, entity);
        entity.setId(dto.getErmbsId());
        entity.setTrainingInstance(dto.getTrainingInstanceId() != null ? trainingInstanceRepository.get(dto.getTrainingInstanceId()) : null);
        entity.setEreimbursementStatus(dto.getStatusCode() != null ? ereimbursementStatusRepository.get(dto.getStatusCode()) : null);
        entity.setIndSxoAmountDueTotal(dto.getIndSxoAmountDueTotal());
        entity.setSxoIndAmountDueTotal(dto.getSxoIndAmountDueTotal());
    }

}
