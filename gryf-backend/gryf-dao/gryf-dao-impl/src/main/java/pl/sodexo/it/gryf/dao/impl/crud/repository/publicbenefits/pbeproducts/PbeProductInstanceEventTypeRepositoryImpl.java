package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.pbeproducts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductEmissionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstanceEventTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstanceEventType;

/**
 * Created by Isolution on 2016-11-03.
 */
@Repository
public class PbeProductInstanceEventTypeRepositoryImpl extends GenericRepositoryImpl<PbeProductInstanceEventType, String> implements PbeProductInstanceEventTypeRepository {

}
