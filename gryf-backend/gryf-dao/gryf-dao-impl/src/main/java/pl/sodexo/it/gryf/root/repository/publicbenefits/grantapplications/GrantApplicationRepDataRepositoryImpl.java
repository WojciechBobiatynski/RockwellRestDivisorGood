package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationRepData;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Repository
public class GrantApplicationRepDataRepositoryImpl extends GenericRepositoryImpl<GrantApplicationRepData, Long> implements GrantApplicationRepDataRepository {
}
