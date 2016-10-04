package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.enterprises;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.ContactDtoMapper;

/**
 * Maper mapujący EnterpriseContactDto na encję EnterpriseContact
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseContactDtoMapper extends ContactDtoMapper<EnterpriseContactDto, EnterpriseContact> {

    @Override
    protected EnterpriseContact initDestination() {
        return new EnterpriseContact();
    }

}
