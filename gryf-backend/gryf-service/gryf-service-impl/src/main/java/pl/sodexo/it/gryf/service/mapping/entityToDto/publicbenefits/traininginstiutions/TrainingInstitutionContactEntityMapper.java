package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.ContactEntityMapper;

/**
 * Mapper mapujący encję TrainingInstitutionContact na dto TrainingInstitutionContactDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionContactEntityMapper extends ContactEntityMapper<TrainingInstitutionContact, TrainingInstitutionContactDto> {

    @Override
    protected TrainingInstitutionContactDto initDestination() {
        return new TrainingInstitutionContactDto();
    }

}
