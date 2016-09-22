package pl.sodexo.it.gryf.service.api.dictionaries;

import pl.sodexo.it.gryf.model.dictionaries.State;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-19.
 */
public interface StateService {

    List<State> findStatesInPoland();
}
