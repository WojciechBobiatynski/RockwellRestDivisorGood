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
     * Znajduje listę wykorzystanych produktów na podstawie id instancji usługi
     * @param trainingInstanceId - id instancji usługi
     * @return lista produktów
     */
    List<ProductHeadDto> findProductsByTrainingInstanceId(Long trainingInstanceId);
}
