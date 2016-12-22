package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementMail;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Mapper encji EreimbursementMail na ErmbsMailDto
 *
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ErmbsMailEntityMapper extends VersionableEntityMapper<EreimbursementMail,ErmbsMailDto> {

    @Override
    protected ErmbsMailDto initDestination() {
        return new ErmbsMailDto();
    }

    @Override
    public void map(EreimbursementMail entity, ErmbsMailDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setErmbsId(entity.getEreimbursement() != null ? entity.getEreimbursement().getId() : null);
        dto.setEmailInstanceId(entity.getEmailInstance() != null ? entity.getEmailInstance().getId() : null);
    }
}
