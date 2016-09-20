package pl.sodexo.it.gryf.validation.dictionaries;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomasz.bilski.ext on 2015-06-29.
 */
public class ContactDataEmailValidator implements ContactDataValidator {

    //STATIC FIELDS

    private static final String PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String MESSAGE = "Nieprawidłowy adres email";

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
