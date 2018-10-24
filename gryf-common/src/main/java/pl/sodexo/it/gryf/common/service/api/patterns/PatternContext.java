package pl.sodexo.it.gryf.common.service.api.patterns;


/**
 * Interejs Kontekstu wzorcow
 *
 * @param <T>
 * @param <Y>
 * @param <Z>
 */
public interface PatternContext<T, Y, Z> {

    T getId();

    Y getCode();

    Z getDefaultPattern();
}
