package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.api.Contact;
import pl.sodexo.it.gryf.service.mapping.entityToDto.AuditableEntityMapper;

/**
 * Maper mapujący encję Contact na ContactDto
 *
 * Created by jbentyn on 2016-10-04.
 */
@Component
public abstract class ContactEntityMapper<Entity extends Contact, Dto extends ContactDto> extends AuditableEntityMapper<Entity, Dto> {

    @Autowired
    private ContactTypeEntityMapper contactTypeEntityMapper;

    @Override
    public void map(Entity entity, Dto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setContactType(contactTypeEntityMapper.convert(entity.getContactType()));
        dto.setContactData(entity.getContactData());
        dto.setRemarks(entity.getRemarks());
    }
}
