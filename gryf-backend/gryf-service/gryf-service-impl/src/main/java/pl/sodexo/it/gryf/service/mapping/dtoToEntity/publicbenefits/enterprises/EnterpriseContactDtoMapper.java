package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.AuditableDtoMapper;

/**
 * Maper mapujący EnterpriseContactDto na encję EnterpriseContact
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseContactDtoMapper  extends AuditableDtoMapper<EnterpriseContactDto,EnterpriseContact>{

    @Autowired
    private ContactTypeDtoMapper contactTypeDtoMapper;

    @Override
    protected EnterpriseContact initDestination() {
        return new EnterpriseContact();
    }

    @Override
    protected void map(EnterpriseContactDto dto, EnterpriseContact entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setContactType(dto.getContactType() == null ? null : contactTypeDtoMapper.convert(dto.getContactType()));
        entity.setContactData(dto.getContactData());
        entity.setRemarks(dto.getRemarks());
    }
}
