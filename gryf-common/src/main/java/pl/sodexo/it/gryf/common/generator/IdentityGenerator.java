package pl.sodexo.it.gryf.common.generator;

/**
 * Interfejs generatora identyfikatorow
 * dla zadanego P obiektu o zadanym typie R
 *
 * @param <P> Obiekt do generowanie identyfikatora
 * @param <R> Typ danych Identyfikatora
 */
public interface IdentityGenerator<P, R> {

    R generate(P context);

}
