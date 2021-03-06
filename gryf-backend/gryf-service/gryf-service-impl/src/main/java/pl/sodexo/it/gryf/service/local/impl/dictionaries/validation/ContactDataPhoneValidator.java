package pl.sodexo.it.gryf.service.local.impl.dictionaries.validation;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.service.local.api.dictionaries.validation.ContactDataValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomasz.bilski.ext on 2015-06-26.
 */
@Component
public class ContactDataPhoneValidator implements ContactDataValidator {

    //STATIC FIELDS

    private static final String PATTERN = "\\d+";

    private static final String MESSAGE = "Numer telefonu musi zawierać tylko cyfry";

    //OVVERRIDE METHODS

    public boolean validate(String value) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

    public String createMessage(String value) {
        return MESSAGE;
    }
}
