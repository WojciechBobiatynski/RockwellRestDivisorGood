package pl.sodexo.it.gryf.service.api.dictionaries;

import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.StateDto;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-19.
 */
public interface StateService {

    List<StateDto> findStatesInPoland();
}
