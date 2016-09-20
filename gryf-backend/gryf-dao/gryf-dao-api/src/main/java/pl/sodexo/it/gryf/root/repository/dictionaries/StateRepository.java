package pl.sodexo.it.gryf.root.repository.dictionaries;

import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface StateRepository extends GenericRepository<State, Long> {

    List<State> findByCountry(Long country);
}
