package pl.sodexo.it.gryf.service.impl.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.StateRepository;
import pl.sodexo.it.gryf.service.api.dictionaries.StateService;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-15.
 */
@Service
@Transactional
public class StateServiceImpl implements StateService {

    //STTAIC FIELDS

    private static final Long COUNTRY_POLAND = 1L;

    //PRIVATE FIELDS

    @Autowired
    private StateRepository stateRepository;

    //PUBLIC METHODS

    @Override
    public List<State> findStatesInPoland(){
        return stateRepository.findByCountry(COUNTRY_POLAND);
    }
}
