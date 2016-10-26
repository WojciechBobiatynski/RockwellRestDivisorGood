package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.individuals;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.ContactDtoMapper;

/**
 * Maper mapujący dto IndividualContactDto na encję IndividualContact
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualContactDtoMapper extends ContactDtoMapper<IndividualContactDto, IndividualContact> {

    @Override
    protected IndividualContact initDestination() {
        return new IndividualContact();
    }

}
