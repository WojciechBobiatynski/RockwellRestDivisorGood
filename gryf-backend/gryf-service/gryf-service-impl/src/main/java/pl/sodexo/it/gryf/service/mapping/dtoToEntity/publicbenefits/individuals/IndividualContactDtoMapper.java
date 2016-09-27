package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.AuditableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.enterprises.ContactTypeDtoMapper;

/**
 * Maper mapujący dto IndividualContactDto na encję IndividualContact
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualContactDtoMapper extends AuditableDtoMapper<IndividualContactDto, IndividualContact> {

    @Autowired
    private ContactTypeDtoMapper contactTypeDtoMapper;

    @Override
    protected IndividualContact initDestination() {
        return new IndividualContact();
    }

    @Override
    protected void map(IndividualContactDto dto, IndividualContact entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setContactType(dto.getContactType() == null ? null : contactTypeDtoMapper.convert(dto.getContactType()));
        entity.setContactData(dto.getContactData());
        entity.setRemarks(dto.getRemarks());
    }
}
