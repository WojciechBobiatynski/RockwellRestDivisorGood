package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationRepDataRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationRepData;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Repository
public class GrantApplicationRepDataRepositoryImpl extends GenericRepositoryImpl<GrantApplicationRepData, Long> implements GrantApplicationRepDataRepository {
}
