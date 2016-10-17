package pl.sodexo.it.gryf.service.api.publicbenefits.products;

/**
 * Serwis obsługujący instancje produktów (pojedyncze bony)
 * Created by jbentyn on 2016-10-12.
 */
public interface ProductInstanceService {

    /**
     * Metoda zleca wygenerowanie numerów drukowanych dla produktu o danym numerze
     *
     * @param productId - unikatowy numer produktu
     */
    void generatePrintNumbersForProduct(String productId);
}
