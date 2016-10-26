package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.ContactEntityMapper;

/**
 * Maper mapujący encję IndividualContact na dto IndividualContactDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualContactEntityMapper extends ContactEntityMapper<IndividualContact, IndividualContactDto> {

    @Override
    protected IndividualContactDto initDestination() {
        return new IndividualContactDto();
    }

}
