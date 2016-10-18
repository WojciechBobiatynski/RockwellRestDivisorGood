package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowElementInStatusRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderFlowElementInStatusRepositoryImpl extends GenericRepositoryImpl<OrderFlowElementInStatus, Long> implements OrderFlowElementInStatusRepository {

    @Override
    public List<OrderFlowElementInStatus> findByOrderStatusIdAndElementId(String orderStatusId, String elementId) {
        TypedQuery<OrderFlowElementInStatus> query = entityManager.createNamedQuery(OrderFlowElementInStatus.FIND_BY_STATUS_ID_AND_ELEMENT_ID, OrderFlowElementInStatus.class);
        query.setParameter("orderStatusId", orderStatusId);
        query.setParameter("elementId", elementId);
        return query.getResultList();
    }


}
