package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramProductRepository extends GenericRepository<GrantProgramProduct, Long> {

    List<GrantProgramProduct> findByGrantProgramInDate(Long grantProgramId, Date date);
}
