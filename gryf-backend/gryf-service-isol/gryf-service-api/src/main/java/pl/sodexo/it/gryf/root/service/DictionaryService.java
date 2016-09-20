package pl.sodexo.it.gryf.root.service;

import pl.sodexo.it.gryf.dto.DictionaryDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface DictionaryService {

    List<DictionaryDTO> findDictionaries(String dictionaryCodeValue);
}
