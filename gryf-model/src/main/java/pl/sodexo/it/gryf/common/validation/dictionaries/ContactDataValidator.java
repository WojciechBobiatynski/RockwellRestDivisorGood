package pl.sodexo.it.gryf.common.validation.dictionaries;

/**
 * Created by tomasz.bilski.ext on 2015-06-26.
 */
public interface ContactDataValidator {

    boolean validate(String value);

    String createMessage(String value);
}
