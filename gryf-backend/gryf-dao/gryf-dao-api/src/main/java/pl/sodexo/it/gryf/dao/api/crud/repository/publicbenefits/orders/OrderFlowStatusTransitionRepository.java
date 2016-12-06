package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowTransitionDTOBuilder;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowStatusTransitionRepository extends GenericRepository<OrderFlowStatusTransition, OrderFlowStatusTransitionPK> {

    List<OrderFlowTransitionDTOBuilder> findDtoByOrder(Long id);
}