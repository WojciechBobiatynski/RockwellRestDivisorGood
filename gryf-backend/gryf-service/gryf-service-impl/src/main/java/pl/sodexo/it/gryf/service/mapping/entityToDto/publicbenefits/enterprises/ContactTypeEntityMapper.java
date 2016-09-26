package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.ContactTypeDto;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;

/**
 * Maper mapujący encję ContactType na ContactTypeDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class ContactTypeEntityMapper extends GryfEntityMapper<ContactType, ContactTypeDto> {

    @Override
    protected ContactTypeDto initDestination() {
        return new ContactTypeDto();
    }

    @Override
    protected void map(ContactType contactType, ContactTypeDto contactTypeDto) {
        super.map(contactType, contactTypeDto);
        contactTypeDto.setType(contactType.getType());
        contactTypeDto.setName(contactType.getName());
        contactTypeDto.setValidationClass(contactType.getValidationClass());
    }
}
