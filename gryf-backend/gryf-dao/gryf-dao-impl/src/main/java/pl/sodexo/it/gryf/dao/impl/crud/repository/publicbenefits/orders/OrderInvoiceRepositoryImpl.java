package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderInvoiceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;

/**
 * Created by Isolution on 2016-12-07.
 */
@Repository
public class OrderInvoiceRepositoryImpl extends GenericRepositoryImpl<OrderInvoice, Long> implements OrderInvoiceRepository {

}
