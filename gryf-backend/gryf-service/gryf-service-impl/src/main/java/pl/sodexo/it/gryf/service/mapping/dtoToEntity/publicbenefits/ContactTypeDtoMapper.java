package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.GryfDtoMapper;

/**
 * Maper mapujący dto ContactTypeDto na encję ContactType
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class ContactTypeDtoMapper  extends GryfDtoMapper<ContactTypeDto,ContactType>{

    @Override
    protected ContactType initDestination() {
        return new ContactType();
    }

    @Override
    protected void map(ContactTypeDto contactTypeDto, ContactType contactType) {
        super.map(contactTypeDto, contactType);
        contactType.setType(contactTypeDto.getType());
        contactType.setName(contactTypeDto.getName());
        contactType.setValidationClass(contactTypeDto.getValidationClass());
    }
}
