/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.formatter;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Marcel.GOLUNSKI
 */
public class DateFormatter implements Formatter<Date> {

    //CONSTRUCTORS

    public DateFormatter() {
    }

    //PUBLIC STATIC METHODS

    public static DateFormat createDateFormat(){
        return StdDateFormat.instance.withTimeZone(TimeZone.getDefault());
    }

    //PUBLIC METHODS

    @Override
    public String print(Date t, Locale locale) {
        return createDateFormat().format(t);
    }

    @Override
    public Date parse(String string, Locale locale) throws ParseException {
        return createDateFormat().parse(string);
    }
    
}
