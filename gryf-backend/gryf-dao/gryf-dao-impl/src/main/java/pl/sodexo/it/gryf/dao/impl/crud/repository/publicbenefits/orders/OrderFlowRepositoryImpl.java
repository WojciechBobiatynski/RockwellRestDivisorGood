package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
@Repository
public class OrderFlowRepositoryImpl extends GenericRepositoryImpl<OrderFlow, Long> implements OrderFlowRepository {

    @Override
    public List<OrderFlow> findByGrantApplicationVersionInDate(Long versionId, Date date) {
        TypedQuery<OrderFlow> query = entityManager.createNamedQuery(OrderFlow.FIND_BY_GRANT_APPLICATION_VERSION_IN_DATE, OrderFlow.class);
        query.setParameter("versionId", versionId);
        query.setParameter("date", date);
        return query.getResultList();
    }

}
