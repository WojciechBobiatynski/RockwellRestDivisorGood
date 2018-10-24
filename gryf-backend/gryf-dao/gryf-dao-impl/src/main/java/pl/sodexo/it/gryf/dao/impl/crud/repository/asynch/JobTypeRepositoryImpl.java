package pl.sodexo.it.gryf.dao.impl.crud.repository.asynch;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.JobTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.asynch.JobType;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tburnicki
 */
@Repository
public class JobTypeRepositoryImpl extends GenericRepositoryImpl<JobType, Long> implements JobTypeRepository {

    @Override
    public JobType findByGrantProgramIdAndName(Long grantProgramId, String name) {
        try {
            TypedQuery<JobType> query = entityManager.createNamedQuery(JobType.QUERY_FIND_BY_GRANT_PROGRAM_ID_AND_NAME, JobType.class);
            query.setParameter(JobType.PARAM_GRANT_PROGRAM_ID, grantProgramId);
            query.setParameter(JobType.PARAM_NAME, name);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public JobType findByName(String name) {
        try {
            TypedQuery<JobType> query = entityManager.createNamedQuery(JobType.QUERY_FIND_BY_NAME, JobType.class);
            query.setParameter(JobType.PARAM_NAME, name);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public List<JobType> findByGrantProgramId(long grantProgramId) {
        TypedQuery<JobType> query = entityManager.createNamedQuery(JobType.QUERY_FIND_BY_GRANT_PROGRAM_ID, JobType.class);
        query.setParameter(JobType.PARAM_GRANT_PROGRAM_ID, grantProgramId);
        return query.getResultList();
    }
}