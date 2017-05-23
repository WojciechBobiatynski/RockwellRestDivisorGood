package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransitionElementFlagRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionElementFlag;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionElementFlagPK;

/**
 * Created by tbilski on 2017-05-21.
 */
@Repository
public class OrderFlowStatusTransitionElementFlagRepositoryImpl extends GenericRepositoryImpl<OrderFlowStatusTransitionElementFlag, OrderFlowStatusTransitionElementFlagPK> implements OrderFlowStatusTransitionElementFlagRepository {
}