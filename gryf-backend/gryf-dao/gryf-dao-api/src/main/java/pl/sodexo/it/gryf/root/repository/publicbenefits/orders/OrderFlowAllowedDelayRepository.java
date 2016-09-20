package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowAllowedDelay;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowAllowedDelayRepository extends GenericRepository<OrderFlowAllowedDelay, Long> {

    List<OrderFlowAllowedDelay> findByOrderFlowAndDelayType(Long orderFlowId, String orderFlowAllowedDelayTypeId);
}
