package pl.sodexo.it.gryf.service.impl.other;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.DictionaryRepository;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.enums.DictionaryCode;
import pl.sodexo.it.gryf.service.api.other.DictionaryService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-02.
 */
@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

    //FIELDS

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    //PUBLIC METHODS

    @Override
    @Cacheable(cacheName = "dictionaryList")
    public List<DictionaryDTO> findDictionaries(String dictionaryCodeValue) {
        DictionaryCode dictionaryCode = DictionaryCode.valueOf(dictionaryCodeValue);

        //ENUM
        if(dictionaryCode.getDictionaryClass().isEnum()){
            DictionaryEntity[] enums = dictionaryCode.getDictionaryClass().getEnumConstants();
            return dictionaryEntityMapper.convert(Arrays.asList(enums));
        }

        //ENTITY
        List<? extends DictionaryEntity> dictionaries = dictionaryRepository.findDictionaries(dictionaryCode.getDictionaryClass());
        return dictionaryEntityMapper.convert(dictionaries);
    }

}
