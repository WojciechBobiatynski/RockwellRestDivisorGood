package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.mapping.entityToDto.AuditableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.ContactTypeEntityMapper;

/**
 * Mapper mapujący encję TrainingInstitutionContact na dto TrainingInstitutionContactDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionContactEntityMapper extends AuditableEntityMapper<TrainingInstitutionContact, TrainingInstitutionContactDto> {

    @Autowired
    private ContactTypeEntityMapper contactTypeEntityMapper;

    @Override
    protected TrainingInstitutionContactDto initDestination() {
        return new TrainingInstitutionContactDto();
    }

    @Override
    public void map(TrainingInstitutionContact entity, TrainingInstitutionContactDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setContactType(contactTypeEntityMapper.convert(entity.getContactType()));
        dto.setContactData(entity.getContactData());
        dto.setRemarks(entity.getRemarks());
    }
}
