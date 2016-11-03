package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.pbeproducts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstanceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstance;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePK;

/**
 * Created by Isolution on 2016-11-03.
 */
@Repository
public class PbeProductInstanceRepositoryImpl extends GenericRepositoryImpl<PbeProductInstance, PbeProductInstancePK> implements PbeProductInstanceRepository {

}
