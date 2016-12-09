package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.pbeproducts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePool;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class PbeProductInstancePoolRepositoryImpl extends GenericRepositoryImpl<PbeProductInstancePool, Long> implements PbeProductInstancePoolRepository {

    @Override
    public List<PbeProductInstancePool> findByIndividualUser(Long userId) {
        TypedQuery<PbeProductInstancePool> query = entityManager.createQuery("select pi from PbeProductInstancePool pi where pi.individual.id = :userId", PbeProductInstancePool.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<PbeProductInstancePool> findAvaiableForUse(Long individualId, Long grantProgramId, Date startDate, Date expiryDate){
        TypedQuery<PbeProductInstancePool> query = entityManager.createNamedQuery("PbeProductInstancePool.findAvaiableForUse", PbeProductInstancePool.class);
        query.setParameter("individualId", individualId);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("startDate", startDate);
        query.setParameter("expiryDate", expiryDate);
        return query.getResultList();
    }
}
