package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.products.PrintNumberChecksumProvider;

/**
 * Testowa implementacja komponentu obliczającego sumę kontrolną dla numerów drukowanych bonów.
 * Implementacja ta zawsze zwraca 0.
 *
 * Created by jbentyn on 2016-10-13.
 */
//TODO podmienić na prawdziwą implementaje jak juz będzie wiadomo jak to zrobić
@Component
public class DummyNumberChecksumProvider implements PrintNumberChecksumProvider {

    @Override
    public Integer generateChecksum(PrintNumberDto printNumberDto) {
        return 0;
    }
}
