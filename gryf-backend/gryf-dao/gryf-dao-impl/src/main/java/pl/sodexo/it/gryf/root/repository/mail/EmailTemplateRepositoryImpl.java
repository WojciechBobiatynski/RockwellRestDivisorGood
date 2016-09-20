package pl.sodexo.it.gryf.root.repository.mail;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class EmailTemplateRepositoryImpl extends GenericRepositoryImpl<EmailTemplate, String> implements EmailTemplateRepository {
}
