package pl.sodexo.it.gryf.root.repository.publicbenefits.enterprises;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseEntityType;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-07-03.
 */
@Repository
public class EnterpriseEntityTypeRepositoryImpl extends GenericRepositoryImpl<EnterpriseEntityType, String> implements EnterpriseEntityTypeRepository {
}
