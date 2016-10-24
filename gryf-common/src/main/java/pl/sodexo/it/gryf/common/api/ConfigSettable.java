package pl.sodexo.it.gryf.common.api;

/**
 * Interfejs klasy, do ktorych ma byc wstrzykiwana konfiguracja przed wykorzystywaniem jej w mybatisie.
 *
 * Created by akmiecinski on 24.10.2016.
 */
public interface ConfigSettable {
    
    /**
     * Metoda ustawiajaca bean konfiguracji.
     * Object ze wzgledu na zaleznosci miedzymodulowe.
     */
    void setConfig(Object config);
}
