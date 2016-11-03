package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.pbeproducts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProduct;

/**
 * Created by Isolution on 2016-11-03.
 */
@Repository
public class PbeProductRepositoryImpl extends GenericRepositoryImpl<PbeProduct, String> implements PbeProductRepository {

}
