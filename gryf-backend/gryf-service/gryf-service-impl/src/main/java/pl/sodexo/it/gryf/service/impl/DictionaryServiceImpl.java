package pl.sodexo.it.gryf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.DictionaryRepository;
import pl.sodexo.it.gryf.model.DictionaryCode;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.service.api.DictionaryService;

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

    //PUBLIC METHODS

    @Override
    public List<DictionaryDTO> findDictionaries(String dictionaryCodeValue) {
        DictionaryCode dictionaryCode = DictionaryCode.valueOf(dictionaryCodeValue);

        //ENUM
        if(dictionaryCode.getDictionaryClass().isEnum()){
            DictionaryEntity[] enums = dictionaryCode.getDictionaryClass().getEnumConstants();
            return DictionaryDTO.createList(Arrays.asList(enums));
        }

        //ENTITY
        List<? extends DictionaryEntity> dictionaries = dictionaryRepository.findDictionaries(dictionaryCode.getDictionaryClass());
        return DictionaryDTO.createList(dictionaries);
    }

}
