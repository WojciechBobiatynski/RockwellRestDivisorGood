package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstance;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePK;

import java.util.List;

/**
 * Created by Isolution on 2016-11-03.
 */
public interface PbeProductInstanceRepository extends GenericRepository<PbeProductInstance, PbeProductInstancePK> {

    List<PbeProductInstance> findAvaiableByProduct(String productId, Integer productInstanceNum);
}
