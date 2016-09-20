package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
@Repository
public class GrantApplicationVersionRepositoryImpl extends GenericRepositoryImpl<GrantApplicationVersion, Long> implements GrantApplicationVersionRepository {

    @Override
    public GrantApplicationVersion findByApplication(Long applicationId){
        TypedQuery<GrantApplicationVersion> query = entityManager.createNamedQuery(GrantApplicationVersion.FIND_BY_APPLICATION, GrantApplicationVersion.class);
        query.setParameter("applicationId", applicationId);
        return query.getSingleResult();
    }

    @Override
    public List<GrantApplicationVersion> findByProgram(Long grantProgramId, Date date){
        TypedQuery<GrantApplicationVersion> query = entityManager.createNamedQuery(GrantApplicationVersion.FIND_BY_PROGRAM_IN_DATE, GrantApplicationVersion.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }

}
