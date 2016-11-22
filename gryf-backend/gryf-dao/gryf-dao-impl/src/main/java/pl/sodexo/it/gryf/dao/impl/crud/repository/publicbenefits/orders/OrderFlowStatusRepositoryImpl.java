package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by adziobek on 22.11.2016.
 */
@Repository
public class OrderFlowStatusRepositoryImpl extends GenericRepositoryImpl<OrderFlowStatus, Long> implements OrderFlowStatusRepository {

    @Override
    public List<OrderFlowStatus> findByGrantProgram(Long grantProgramId) {
        TypedQuery<OrderFlowStatus> query = entityManager.createNamedQuery("OrderFlowStatus.findByGrantProgram", OrderFlowStatus.class);
        query.setParameter("grantProgramId", grantProgramId);
        return query.getResultList();
    }
}