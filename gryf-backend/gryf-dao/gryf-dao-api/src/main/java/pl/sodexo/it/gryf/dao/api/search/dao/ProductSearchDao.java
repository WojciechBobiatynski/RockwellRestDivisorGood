package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.ProductHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;

import java.util.List;

/**
 * Dao do wyszukiwania produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductSearchDao {

    /**
     * Wyszukuje listę ważnych produktów
     *
     * @return lista produktów
     */
    List<ProductDto> findProducts();

    /**
     * Znajduje listę wykorzystanych produktów na podstawie id instancji szkolenia
     * @param trainingInstanceId - id instancji szkolenia
     * @return lista produktów
     */
    List<ProductHeadDto> findProductsByTrainingInstanceId(Long trainingInstanceId);
}
