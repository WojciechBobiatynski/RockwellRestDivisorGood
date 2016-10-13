package pl.sodexo.it.gryf.service.local.api.publicbenefits.products;

import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;

/**
 * Interfejs dla klas obliczających sumę kontrolna dla  numerów drukowanych bonów
 *
 * Created by jbentyn on 2016-10-13.
 */
public interface PrintNumberChecksumProvider {

    /**
     * Generuje sumę kontrolną numeru drukowanego bonu.
     *
     * @param printNumberDto - dto zawierające potrzebne do stworzenia numeru drukowanego bonu
     *
     * @return Oblczona suma kontrolna
     */
    Integer generateChecksum(PrintNumberDto printNumberDto);
}
