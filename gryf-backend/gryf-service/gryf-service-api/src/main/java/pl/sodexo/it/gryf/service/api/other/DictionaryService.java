package pl.sodexo.it.gryf.service.api.other;

import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface DictionaryService {

    List<DictionaryDTO> findDictionaries(String dictionaryCodeValue);
}
