package pl.sodexo.it.gryf.model.api;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
public interface DictionaryEntity {

    Object getDictionaryId();

    String getDictionaryName();

    String getOrderField();

    String getActiveField();

}
