package pl.sodexo.it.gryf.service.api.patterns;


import java.io.Serializable;

/**
 * Interejs Kontekstu wzorcow
 *
 * @param <T>
 * @param <Y>
 * @param <Z>
 */
public interface PatternContext<T, Y, Z> extends Serializable {

    T getId();

    Y getCode();

    Z getDefaultPattern();
}
