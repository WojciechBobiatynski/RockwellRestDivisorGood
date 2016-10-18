package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantApplicationVersionRepository extends GenericRepository<GrantApplicationVersion, Long> {

    GrantApplicationVersion findByApplication(Long applicationId);

    List<GrantApplicationVersion> findByProgram(Long grantProgramId, Date date);
}
