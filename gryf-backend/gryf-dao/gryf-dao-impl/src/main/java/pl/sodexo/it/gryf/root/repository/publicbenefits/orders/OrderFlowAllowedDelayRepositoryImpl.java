package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowAllowedDelay;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderFlowAllowedDelayRepositoryImpl extends GenericRepositoryImpl<OrderFlowAllowedDelay, Long> implements OrderFlowAllowedDelayRepository {

    @Override
    public List<OrderFlowAllowedDelay> findByOrderFlowAndDelayType(Long orderFlowId, String orderFlowAllowedDelayTypeId) {
        TypedQuery<OrderFlowAllowedDelay> query = entityManager.createNamedQuery(OrderFlowAllowedDelay.FIND_BY_ORDER_FLOW_AND_DELAY_TYPE, OrderFlowAllowedDelay.class);
        query.setParameter("orderFlowId", orderFlowId);
        query.setParameter("orderFlowAllowedDelayTypeId", orderFlowAllowedDelayTypeId);
        return query.getResultList();
    }


}
