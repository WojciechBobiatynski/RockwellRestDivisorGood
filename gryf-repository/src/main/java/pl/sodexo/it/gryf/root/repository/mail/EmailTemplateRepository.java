package pl.sodexo.it.gryf.root.repository.mail;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class EmailTemplateRepository extends GenericRepository<EmailTemplate, String>{
}
