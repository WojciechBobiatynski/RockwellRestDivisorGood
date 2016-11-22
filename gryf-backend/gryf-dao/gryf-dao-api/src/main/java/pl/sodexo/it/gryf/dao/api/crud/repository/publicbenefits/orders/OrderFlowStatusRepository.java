package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;

import java.util.List;

/**
 * Created by adziobek on 22.11.2016.
 */
public interface OrderFlowStatusRepository extends GenericRepository<OrderFlowStatus, Long> {

    List<OrderFlowStatus> findByGrantProgram(Long grantProgramId);
}