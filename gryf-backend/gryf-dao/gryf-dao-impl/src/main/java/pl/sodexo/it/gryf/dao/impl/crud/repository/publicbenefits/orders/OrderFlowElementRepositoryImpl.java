package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowElementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-08-28.
 */
@Repository
public class OrderFlowElementRepositoryImpl extends GenericRepositoryImpl<OrderFlowElement, String> implements OrderFlowElementRepository {
}
