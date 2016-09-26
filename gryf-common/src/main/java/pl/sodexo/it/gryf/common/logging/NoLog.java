package pl.sodexo.it.gryf.common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja pozwalajaca wylaczyc generyczne logowanie.
 *
 * Created by adziobek on 26.09.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface NoLog {

}