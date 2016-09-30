package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransitionRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowTransitionDTOBuilder;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-27.
 */
@Repository
public class OrderFlowStatusTransitionRepositoryImpl extends GenericRepositoryImpl<OrderFlowStatusTransition, OrderFlowStatusTransitionPK> implements OrderFlowStatusTransitionRepository {

    @Override
    public List<OrderFlowTransitionDTOBuilder> findDtoByOrder(Long id) {
        TypedQuery<OrderFlowTransitionDTOBuilder> query = entityManager.createNamedQuery(OrderFlowStatusTransition.FIND_DTO_BY_ORDER, OrderFlowTransitionDTOBuilder.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
