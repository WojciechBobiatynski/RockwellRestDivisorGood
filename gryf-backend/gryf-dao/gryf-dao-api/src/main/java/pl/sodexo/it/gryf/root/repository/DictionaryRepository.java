package pl.sodexo.it.gryf.root.repository;

import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.DictionaryEntity;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface DictionaryRepository {

    /**
     * Zwraca liste encji słownikowych na podstawie klasy encji slownikowej.
     * @param clazz klasa encji słownikowej
     * @return lista objektów klasy encji słownikowej
     */
    List<? extends DictionaryEntity> findDictionaries(Class<? extends DictionaryEntity> clazz);

    /**
     * Zwraca listę słowników na podstawie natywnego sql który zwraca dwie wartosci.
     * @param nativeSql natywny sql
     * @return lista obiektow slownikowych
     */
    List<DictionaryDTO> findDictionaries(String nativeSql);

    DictionaryEntity createEmptyDictionary(Class<? extends DictionaryEntity> clazz);
}
