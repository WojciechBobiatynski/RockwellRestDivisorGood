package pl.sodexo.it.gryf.dao.api.search.mapper;

import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;

import java.util.List;

/**
 * Mapper batisowy dla produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductSearchMapper {

    /**
     * Znajduje listę ważnych produktów
     * @return lista produktów
     */
    List<ProductDto> findProducts();
}
