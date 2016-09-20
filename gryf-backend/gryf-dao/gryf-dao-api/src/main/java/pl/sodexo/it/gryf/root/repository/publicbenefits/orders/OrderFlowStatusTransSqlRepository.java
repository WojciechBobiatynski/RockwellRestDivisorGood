package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransSql;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowStatusTransSqlRepository extends GenericRepository<OrderFlowStatusTransSql, Long> {

    void executeNativeSql(OrderFlowStatusTransSql transSql, Long orderId, String login);
}
