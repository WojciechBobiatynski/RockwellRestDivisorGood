package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderFlowElementInStatusRepository extends GenericRepository<OrderFlowElementInStatus, Long> {

    public List<OrderFlowElementInStatus> findByOrderStatusIdAndElementId(String orderStatusId, String elementId) {
        TypedQuery<OrderFlowElementInStatus> query = entityManager.createNamedQuery(OrderFlowElementInStatus.FIND_BY_STATUS_ID_AND_ELEMENT_ID, OrderFlowElementInStatus.class);
        query.setParameter("orderStatusId", orderStatusId);
        query.setParameter("elementId", elementId);
        return query.getResultList();
    }


}
