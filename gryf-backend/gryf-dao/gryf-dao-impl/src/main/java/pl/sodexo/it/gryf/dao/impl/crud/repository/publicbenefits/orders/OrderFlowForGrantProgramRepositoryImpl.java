package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantProgramRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantProgram;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-18.
 */
@Repository
public class OrderFlowForGrantProgramRepositoryImpl extends GenericRepositoryImpl<OrderFlowForGrantProgram, Long>
                                                    implements OrderFlowForGrantProgramRepository {

    @Override
    public List<OrderFlowForGrantProgram> findByGrantProgramInDate(Long grantProgramId, Date date){
        TypedQuery<OrderFlowForGrantProgram> query = entityManager.createNamedQuery("OrderFlowForGrantProgram.findByGrantProgramInDate", OrderFlowForGrantProgram.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
