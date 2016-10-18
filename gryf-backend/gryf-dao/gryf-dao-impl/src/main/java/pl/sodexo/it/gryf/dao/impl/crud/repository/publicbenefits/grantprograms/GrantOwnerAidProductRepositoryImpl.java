package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantprograms;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantOwnerAidProductRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwnerAidProduct;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
@Repository
public class GrantOwnerAidProductRepositoryImpl extends GenericRepositoryImpl<GrantOwnerAidProduct, String> implements GrantOwnerAidProductRepository {
}
