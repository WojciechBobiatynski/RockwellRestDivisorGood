package pl.sodexo.it.gryf.dao.impl.crud.repository.dictionaries;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.StateRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.dictionaries.State;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-15.
 */
@Repository
public class StateRepositoryImpl extends GenericRepositoryImpl<State, Long> implements StateRepository {

    @Override
    public List<State> findByCountry(Long country){
        TypedQuery<State> query = entityManager.createNamedQuery(State.FIND_BY_COUNTRY, State.class);
        query.setParameter("country", country);
        return query.getResultList();
    }

}
