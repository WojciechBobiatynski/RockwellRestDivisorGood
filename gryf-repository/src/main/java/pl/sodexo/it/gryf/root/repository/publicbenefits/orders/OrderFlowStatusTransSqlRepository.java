package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransSql;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.Query;

/**
 * Created by tomasz.bilski.ext on 2015-08-25.
 */
@Repository
public class OrderFlowStatusTransSqlRepository extends GenericRepository<OrderFlowStatusTransSql, Long> {

    public void executeNativeSql(OrderFlowStatusTransSql transSql, Long orderId, String login){
        Query query = entityManager.createNativeQuery(transSql.getSql());
        query.setParameter(1, orderId);
        query.setParameter(2, login);
        try {
            query.executeUpdate();
        }catch(RuntimeException e){
            throw new RuntimeException(String.format("Wystapił błąd przy wywolaniu zapytania sql. Identyfikator sql = '%s'", transSql.getId()), e);
        }
    }
}
