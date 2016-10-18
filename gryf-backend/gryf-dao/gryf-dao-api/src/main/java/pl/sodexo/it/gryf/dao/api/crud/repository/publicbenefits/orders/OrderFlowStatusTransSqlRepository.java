package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransSql;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowStatusTransSqlRepository extends GenericRepository<OrderFlowStatusTransSql, Long> {

    void executeNativeSql(OrderFlowStatusTransSql transSql, Long orderId, String login);
}
