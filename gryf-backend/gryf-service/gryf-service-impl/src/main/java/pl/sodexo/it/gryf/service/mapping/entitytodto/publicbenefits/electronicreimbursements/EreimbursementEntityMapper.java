package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Mapper mapujący encję na dto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class EreimbursementEntityMapper extends VersionableEntityMapper<Ereimbursement, ElctRmbsHeadDto> {

    @Override
    protected ElctRmbsHeadDto initDestination() {
        return new ElctRmbsHeadDto();
    }

    @Override
    public void map(Ereimbursement entity, ElctRmbsHeadDto dto) {
        super.map(entity, dto);
    }

}
