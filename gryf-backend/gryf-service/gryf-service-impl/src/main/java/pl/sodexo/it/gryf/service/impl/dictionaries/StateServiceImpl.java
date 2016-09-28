package pl.sodexo.it.gryf.service.impl.dictionaries;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.StateDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.StateRepository;
import pl.sodexo.it.gryf.service.api.dictionaries.StateService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.StateEntityMapper;

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

    @Autowired
    private StateEntityMapper stateEntityMapper;

    //PUBLIC METHODS

    @Override
    @Cacheable(cacheName = "statesInPolandList")
    public List<StateDto> findStatesInPoland(){
        return stateEntityMapper.convert(stateRepository.findByCountry(COUNTRY_POLAND));
    }
}
