package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderFlowRepository extends GenericRepository<OrderFlow, Long> {

    List<OrderFlow> findByGrantApplicationVersionInDate(Long versionId, Date date);
}
