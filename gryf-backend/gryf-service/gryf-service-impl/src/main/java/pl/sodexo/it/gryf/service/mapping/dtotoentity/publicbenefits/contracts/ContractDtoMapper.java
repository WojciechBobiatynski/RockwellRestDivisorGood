package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.contracts;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Maper mapujący ContractDto na encję Contract
 *
 * Created by adziobek on 31.10.2016.
 */
@Component
public class ContractDtoMapper extends VersionableDtoMapper<ContractDTO,Contract> {

    @Override
    protected Contract initDestination() {
        return new Contract();
    }

    protected void map(ContractDTO dto, Contract entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setSignDate(dto.getSignDate());
        entity.setExpiryDate(dto.getExpiryDate());
    }

}