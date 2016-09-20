package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-01.
 */
@Repository
public class GrantApplicationVersionRepository extends GenericRepository<GrantApplicationVersion, Long> {

    public GrantApplicationVersion findByApplication(Long applicationId){
        TypedQuery<GrantApplicationVersion> query = entityManager.createNamedQuery(GrantApplicationVersion.FIND_BY_APPLICATION, GrantApplicationVersion.class);
        query.setParameter("applicationId", applicationId);
        return query.getSingleResult();
    }

    public List<GrantApplicationVersion> findByProgram(Long grantProgramId, Date date){
        TypedQuery<GrantApplicationVersion> query = entityManager.createNamedQuery(GrantApplicationVersion.FIND_BY_PROGRAM_IN_DATE, GrantApplicationVersion.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }

}
