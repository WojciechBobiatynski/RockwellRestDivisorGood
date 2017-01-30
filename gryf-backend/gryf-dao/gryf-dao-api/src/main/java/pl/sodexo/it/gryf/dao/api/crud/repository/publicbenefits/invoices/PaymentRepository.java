package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.invoices;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.Payment;

import java.util.List;

/**
 * Created by Isolution on 2017-01-30.
 */
public interface PaymentRepository extends GenericRepository<Payment, Long> {

    List<Payment> findByOrder(Long orderId);
}
