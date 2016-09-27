package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.IndividualContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.mapping.entityToDto.AuditableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.ContactTypeEntityMapper;

/**
 * Maper mapujący encję IndividualContact na dto IndividualContactDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualContactEntityMapper extends AuditableEntityMapper<IndividualContact, IndividualContactDto> {

    @Autowired
    private ContactTypeEntityMapper contactTypeEntityMapper;

    @Override
    protected IndividualContactDto initDestination() {
        return new IndividualContactDto();
    }

    @Override
    public void map(IndividualContact entity, IndividualContactDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setContactType(contactTypeEntityMapper.convert(entity.getContactType()));
        dto.setContactData(entity.getContactData());
        dto.setRemarks(entity.getRemarks());
    }
}
