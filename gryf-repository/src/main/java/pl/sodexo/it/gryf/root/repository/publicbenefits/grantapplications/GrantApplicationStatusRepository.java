package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationStatus;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
@Repository
public class GrantApplicationStatusRepository extends GenericRepository<GrantApplicationStatus, String> {
}
