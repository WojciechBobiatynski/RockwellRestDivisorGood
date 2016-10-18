package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationInProgramRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationInProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationInProgramPK;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
@Repository
public class GrantApplicationInProgramRepositoryImpl extends GenericRepositoryImpl<GrantApplicationInProgram, GrantApplicationInProgramPK> implements GrantApplicationInProgramRepository {
}
