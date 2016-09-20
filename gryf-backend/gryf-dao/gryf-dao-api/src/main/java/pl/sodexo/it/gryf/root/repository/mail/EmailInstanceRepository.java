package pl.sodexo.it.gryf.root.repository.mail;

import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface EmailInstanceRepository extends GenericRepository<EmailInstance, Long> {

    List<EmailInstance> findByStatus(String status);
}
