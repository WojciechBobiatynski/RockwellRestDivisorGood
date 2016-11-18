package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantApplicationVersionRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-18.
 */
@Repository
public class OrderFlowForGrantApplicationVersionRepositoryImpl extends GenericRepositoryImpl<OrderFlowForGrantApplicationVersion, Long>
                                                                implements OrderFlowForGrantApplicationVersionRepository {
    @Override
    public List<OrderFlowForGrantApplicationVersion> findByGrantApplicationVersionInDate(Long versionId, Date date) {
        TypedQuery<OrderFlowForGrantApplicationVersion> query = entityManager.createNamedQuery("OrderFlowForGrantApplicationVersion.findByGrantApplicationVersionInDate", OrderFlowForGrantApplicationVersion.class);
        query.setParameter("versionId", versionId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
