package pl.sodexo.it.gryf.service.api.publicbenefits.products;

import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;

import java.util.List;

/**
 * Serwis dla produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductService {

    /**
     * Wyszukuje listę produktów, które nie sa przeterminowane
     *
     * @return lista produktów
     */
    List<ProductDto> findProducts();
}
