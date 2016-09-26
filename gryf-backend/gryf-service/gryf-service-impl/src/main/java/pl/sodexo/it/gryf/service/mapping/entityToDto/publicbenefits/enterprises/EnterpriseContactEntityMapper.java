package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.mapping.entityToDto.AuditableEntityMapper;

/**
 * Maper mapujący encję EnterpriseContact na EnterpriseContactDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseContactEntityMapper extends AuditableEntityMapper<EnterpriseContact,EnterpriseContactDto>{

    @Autowired
    private ContactTypeEntityMapper contactTypeEntityMapper;

    @Override
    protected EnterpriseContactDto initDestination() {
        return new EnterpriseContactDto();
    }

    @Override
    public void map(EnterpriseContact entity, EnterpriseContactDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setContactType(contactTypeEntityMapper.convert(entity.getContactType()));
        dto.setContactData(entity.getContactData());
        dto.setRemarks(entity.getRemarks());
        dto.setEnterpriseId(entity.getEnterprise().getId());
    }
}
