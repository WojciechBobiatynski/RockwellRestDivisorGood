package pl.sodexo.it.gryf.common.annotation;

import java.lang.annotation.*;

/**
 * Anotacja na metody, które mają nie być logowane przy pomocy aspektu LogginAspect
 *
 * Created by akmiecinski on 20.12.2016.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface LoggingDisabled {

}
