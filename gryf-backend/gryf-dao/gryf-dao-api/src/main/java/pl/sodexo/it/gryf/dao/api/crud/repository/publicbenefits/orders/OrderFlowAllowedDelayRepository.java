package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowAllowedDelay;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowAllowedDelayRepository extends GenericRepository<OrderFlowAllowedDelay, Long> {

    List<OrderFlowAllowedDelay> findByOrderFlowAndDelayType(Long orderFlowId, String orderFlowAllowedDelayTypeId);
}
