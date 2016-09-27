package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.AuditableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.enterprises.ContactTypeDtoMapper;

/**
 * Mapper mapujący dto TrainingInstitutionContactDto na encję TrainingInstitutionContact
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionContactDtoMapper extends AuditableDtoMapper<TrainingInstitutionContactDto, TrainingInstitutionContact> {

    @Autowired
    private ContactTypeDtoMapper contactTypeDtoMapper;

    @Override
    protected TrainingInstitutionContact initDestination() {
        return new TrainingInstitutionContact();
    }

    @Override
    protected void map(TrainingInstitutionContactDto dto, TrainingInstitutionContact entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setContactType(dto.getContactType() == null ? null : contactTypeDtoMapper.convert(dto.getContactType()));
        entity.setContactData(dto.getContactData());
        entity.setRemarks(dto.getRemarks());
    }
}
