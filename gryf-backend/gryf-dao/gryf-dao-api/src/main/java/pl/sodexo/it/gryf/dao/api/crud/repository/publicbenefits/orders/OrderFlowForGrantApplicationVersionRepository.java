package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-18.
 */
public interface OrderFlowForGrantApplicationVersionRepository extends GenericRepository<OrderFlowForGrantApplicationVersion, Long> {

    List<OrderFlowForGrantApplicationVersion> findByGrantApplicationVersionInDate(Long versionId, Date date);
}
