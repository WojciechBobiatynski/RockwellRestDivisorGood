package pl.sodexo.it.gryf.dao.impl.crud.repository.mail;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailInstanceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.mail.EmailInstance;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class EmailInstanceRepositoryImpl extends GenericRepositoryImpl<EmailInstance, Long> implements EmailInstanceRepository {

    @Override
    public List<EmailInstance> findAvailableToSend(String status){
        TypedQuery<EmailInstance> query = entityManager.createNamedQuery("EmailInstance.findByStatus", EmailInstance.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
