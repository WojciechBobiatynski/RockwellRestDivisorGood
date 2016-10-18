package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationStatusRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationStatus;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
@Repository
public class GrantApplicationStatusRepositoryImpl extends GenericRepositoryImpl<GrantApplicationStatus, String> implements GrantApplicationStatusRepository {
}
