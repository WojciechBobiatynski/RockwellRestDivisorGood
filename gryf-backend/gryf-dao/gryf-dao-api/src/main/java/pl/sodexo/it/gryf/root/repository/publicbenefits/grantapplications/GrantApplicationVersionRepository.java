package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantApplicationVersionRepository extends GenericRepository<GrantApplicationVersion, Long> {

    GrantApplicationVersion findByApplication(Long applicationId);

    List<GrantApplicationVersion> findByProgram(Long grantProgramId, Date date);
}
