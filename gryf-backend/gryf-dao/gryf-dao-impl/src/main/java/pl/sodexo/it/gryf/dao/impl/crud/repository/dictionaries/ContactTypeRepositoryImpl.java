package pl.sodexo.it.gryf.dao.impl.crud.repository.dictionaries;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ContactTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;

/**
 * Created by tomasz.bilski.ext on 2015-06-19.
 */
@Repository
public class ContactTypeRepositoryImpl extends GenericRepositoryImpl<ContactType, String> implements ContactTypeRepository {
}