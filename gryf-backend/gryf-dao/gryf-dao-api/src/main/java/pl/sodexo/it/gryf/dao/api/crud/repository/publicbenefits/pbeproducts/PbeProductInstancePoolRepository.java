package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePool;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
public interface PbeProductInstancePoolRepository extends GenericRepository<PbeProductInstancePool, Long> {

    List<PbeProductInstancePool> findByIndividualUser(Long userId);

    List<PbeProductInstancePool> findAvaiableForUse(Long individualId, Long grantProgramId, Date startDate, Date expiryDate);
}
