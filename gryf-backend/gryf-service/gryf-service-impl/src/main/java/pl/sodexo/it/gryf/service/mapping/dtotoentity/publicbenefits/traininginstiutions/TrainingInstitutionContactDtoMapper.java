package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.ContactDtoMapper;

/**
 * Mapper mapujący dto TrainingInstitutionContactDto na encję TrainingInstitutionContact
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionContactDtoMapper extends ContactDtoMapper<TrainingInstitutionContactDto, TrainingInstitutionContact> {

    @Override
    protected TrainingInstitutionContact initDestination() {
        return new TrainingInstitutionContact();
    }

}
