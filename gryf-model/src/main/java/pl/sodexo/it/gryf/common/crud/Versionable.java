package pl.sodexo.it.gryf.common.crud;

/**
 * Interfejs oznaczajacy klase z wersja.
 *
 * Created by akmiecinski on 2016-10-03.
 */
public interface Versionable extends Auditable {

    /**
     * Zwraca wersję obiektu
     *
     * @return wersja
     */
    Integer getVersion();
}
