package pl.sodexo.it.gryf.root.repository.dictionaries;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-06-19.
 */
@Repository
public class ContactTypeRepositoryImpl extends GenericRepositoryImpl<ContactType, String> implements ContactTypeRepository {
}