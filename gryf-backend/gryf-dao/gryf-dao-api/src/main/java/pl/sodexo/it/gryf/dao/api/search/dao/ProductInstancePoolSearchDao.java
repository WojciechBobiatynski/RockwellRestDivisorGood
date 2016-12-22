package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

import java.util.List;

/**
 * Search dao dla puli instancji produktów (bonów)
 *
 * Created by akmiecinski on 2016-12-22.
 */
public interface ProductInstancePoolSearchDao {

    /**
     * Znajduje wszystkie przeterminowane pule bonów dla kierunku kariera
     * @return lista pul bonów
     */
    List<PbeProductInstancePoolDto> findExpiredPoolInstances();

}
