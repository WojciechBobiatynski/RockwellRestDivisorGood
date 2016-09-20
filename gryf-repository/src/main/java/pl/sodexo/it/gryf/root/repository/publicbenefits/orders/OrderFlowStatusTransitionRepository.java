package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-27.
 */
@Repository
public class OrderFlowStatusTransitionRepository extends GenericRepository<OrderFlowStatusTransition, OrderFlowStatusTransitionPK> {

    public List<OrderFlowTransitionDTO> findDtoByOrder(Long id) {
        TypedQuery<OrderFlowTransitionDTO> query = entityManager.createNamedQuery(OrderFlowStatusTransition.FIND_DTO_BY_ORDER, OrderFlowTransitionDTO.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
