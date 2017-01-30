package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.invoices;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.invoices.PaymentRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.Payment;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2017-01-30.
 */
@Repository
public class PaymentRepositoryImpl extends GenericRepositoryImpl<Payment, Long> implements PaymentRepository {

    @Override
    public List<Payment> findByOrder(Long orderId){
        TypedQuery<Payment> query = entityManager.createNamedQuery("Payment.findByOrder", Payment.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
