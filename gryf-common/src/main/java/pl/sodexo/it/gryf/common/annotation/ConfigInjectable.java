package pl.sodexo.it.gryf.common.annotation;

import java.lang.annotation.*;

/**
 * Anotacja na klasy, do ktorych ma byc wstrzykiwany bean ApplicationParameters
 * przed wykorzystywaniem ich w mybatisie
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)       
public @interface ConfigInjectable {

}