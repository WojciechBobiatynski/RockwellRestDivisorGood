package pl.sodexo.it.gryf.validation;

/**
 * Created by tomasz.bilski.ext on 2015-09-03.
 */

import pl.sodexo.it.gryf.Privileges;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InsertablePrivilege {

    public Privileges[] privileges() default {};

    public String message() default "Nie posiadasz uprawnień do edycji tego pola";

}


