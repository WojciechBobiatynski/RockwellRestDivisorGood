package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.pbeproductinstancepool;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePool;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

@Component
public class PbeProductInstancePoolEntityMapper extends VersionableEntityMapper<PbeProductInstancePool, PbeProductInstancePoolDto> {

    @Override
    protected PbeProductInstancePoolDto initDestination() {
        return new PbeProductInstancePoolDto();
    }

    @Override
    public void map(PbeProductInstancePool entity, PbeProductInstancePoolDto dto) {
        super.map(entity, dto);

        dto.setId(entity.getId());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setAvailableNum(entity.getAvailableNum());
    }
}
