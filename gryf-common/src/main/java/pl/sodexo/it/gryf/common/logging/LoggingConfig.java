package pl.sodexo.it.gryf.common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja pozwalajaca skonfigurowac poziom logowania wywolania poszczegolnej metody lub argumentu metody.
 *
 * Created by adziobek on 26.09.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface LoggingConfig {

    LoggingLevel value() default LoggingLevel.TRACE;

    boolean logOutput() default true;

}