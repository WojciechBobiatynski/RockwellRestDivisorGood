package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowElementInStatusRepository extends GenericRepository<OrderFlowElementInStatus, Long> {

    List<OrderFlowElementInStatus> findByOrderStatusIdAndElementId(String orderStatusId, String elementId);
}
