package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.pbeproducts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstanceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstance;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePK;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstanceStatus;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-11-03.
 */
@Repository
public class PbeProductInstanceRepositoryImpl extends GenericRepositoryImpl<PbeProductInstance, PbeProductInstancePK> implements PbeProductInstanceRepository {

    public List<PbeProductInstance> findAvaiableByProduct(String productId, Integer productInstanceNum){
        TypedQuery<PbeProductInstance> query = entityManager.createNamedQuery("PbeProductInstance.findAvaiableByProduct", PbeProductInstance.class);
        query.setParameter("productId", productId);
        query.setParameter("statusId", PbeProductInstanceStatus.NEW_CODE);
        query.setMaxResults(productInstanceNum);
        return query.getResultList();
    }

    public List<PbeProductInstance> findAssignedByPool(Long poolId, Integer productInstanceNum){
        TypedQuery<PbeProductInstance> query = entityManager.createNamedQuery("PbeProductInstance.findAssignedByPool", PbeProductInstance.class);
        query.setParameter("poolId", poolId);
        query.setParameter("statusId", PbeProductInstanceStatus.ASSIGN_CODE);
        query.setMaxResults(productInstanceNum);
        return query.getResultList();
    }

}
