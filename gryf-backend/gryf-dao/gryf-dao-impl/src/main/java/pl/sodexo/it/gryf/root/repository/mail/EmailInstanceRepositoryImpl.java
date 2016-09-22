package pl.sodexo.it.gryf.root.repository.mail;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class EmailInstanceRepositoryImpl extends GenericRepositoryImpl<EmailInstance, Long> implements EmailInstanceRepository {

    @Override
    public List<EmailInstance> findByStatus(String status){
        TypedQuery<EmailInstance> query = entityManager.createNamedQuery(EmailInstance.FIND_BY_STATUS, EmailInstance.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}