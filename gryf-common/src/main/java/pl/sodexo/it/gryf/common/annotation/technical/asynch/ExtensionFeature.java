package pl.sodexo.it.gryf.common.annotation.technical.asynch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja identyfikujaca kod do rozubudowy
 * Created by tburnicki on 12.10.2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface ExtensionFeature {

    String desc();

}