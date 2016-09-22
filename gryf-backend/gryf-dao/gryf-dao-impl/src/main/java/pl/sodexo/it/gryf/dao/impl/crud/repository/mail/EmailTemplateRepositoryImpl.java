package pl.sodexo.it.gryf.dao.impl.crud.repository.mail;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailTemplateRepository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class EmailTemplateRepositoryImpl extends GenericRepositoryImpl<EmailTemplate, String> implements EmailTemplateRepository {
}
