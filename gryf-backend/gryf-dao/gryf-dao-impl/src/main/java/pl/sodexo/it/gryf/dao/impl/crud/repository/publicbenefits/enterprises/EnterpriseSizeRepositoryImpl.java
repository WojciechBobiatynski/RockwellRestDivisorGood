package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.enterprises;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseSizeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-07-03.
 */
@Repository
public class EnterpriseSizeRepositoryImpl extends GenericRepositoryImpl<EnterpriseSize, String> implements EnterpriseSizeRepository {
}
