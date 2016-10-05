package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.api.Contact;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.AuditableDtoMapper;

/**
 * Maper mapujący ContactDto na encję Contact
 *
 * Created by jbentyn on 2016-10-04.
 */
@Component
public abstract class ContactDtoMapper<Dto extends ContactDto, Entity extends Contact> extends AuditableDtoMapper<Dto, Entity> {

    @Autowired
    private ContactTypeDtoMapper contactTypeDtoMapper;

    @Override
    protected void map(Dto dto, Entity entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setContactType(dto.getContactType() == null ? null : contactTypeDtoMapper.convert(dto.getContactType()));
        entity.setContactData(dto.getContactData());
        entity.setRemarks(dto.getRemarks());
    }
}
