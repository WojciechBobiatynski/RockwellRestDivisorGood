package pl.sodexo.it.gryf.dao.impl.crud.repository.asynch;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobStatus;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-12-01.
 */
@Repository
public class AsynchronizeJobRepositoryImpl extends GenericRepositoryImpl<AsynchronizeJob, Long> implements AsynchronizeJobRepository {

    @Override
    public AsynchronizeJob findFirstAsynchronizeJobToWork(){
        TypedQuery<AsynchronizeJob> query = entityManager.createNamedQuery("AsynchronizeJob.findFirstAsynchronizeJobToWork", AsynchronizeJob.class);
        query.setParameter("status", AsynchronizeJobStatus.N);
        query.setMaxResults(1);
        List<AsynchronizeJob> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}
