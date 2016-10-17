package pl.sodexo.it.gryf.dao.api.search.dao;

/**
 * Search dao dla instancji produktów (bonów)
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductInstanceSearchDao {

    /**
     * Zwraca liczbe bonów, które sa gotowe do generowania numeru drukowanego
     *
     * @param productId - unikatowy numer produktu
     * @return liczba bonów gotowych do generacji numeru
     */
    Long countProductInstancesAvailableToNumberGeneration(String productId);
}
