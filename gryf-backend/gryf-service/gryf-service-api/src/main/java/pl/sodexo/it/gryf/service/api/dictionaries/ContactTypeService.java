package pl.sodexo.it.gryf.service.api.dictionaries;

import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-19.
 */
public interface ContactTypeService {

    List<ContactTypeDto> findContactTypes();

}
