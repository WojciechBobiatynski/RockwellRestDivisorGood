package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.ContactEntityMapper;

/**
 * Maper mapujący encję EnterpriseContact na EnterpriseContactDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseContactEntityMapper extends ContactEntityMapper<EnterpriseContact, EnterpriseContactDto> {

    @Override
    protected EnterpriseContactDto initDestination() {
        return new EnterpriseContactDto();
    }

}
