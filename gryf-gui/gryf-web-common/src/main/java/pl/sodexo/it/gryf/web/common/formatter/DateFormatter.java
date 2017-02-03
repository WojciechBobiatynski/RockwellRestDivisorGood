package pl.sodexo.it.gryf.web.common.formatter;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Klasa formatujaca datę
 */
public class DateFormatter implements Formatter<Date> {

    public static DateFormat createDateFormat(){
        return StdDateFormat.instance.withTimeZone(TimeZone.getDefault());
    }

    @Override
    public String print(Date t, Locale locale) {
        return createDateFormat().format(t);
    }

    @Override
    public Date parse(String string, Locale locale) throws ParseException {
        return createDateFormat().parse(string);
    }
}
