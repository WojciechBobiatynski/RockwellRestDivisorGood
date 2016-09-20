package pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@Repository
public class GrantProgramParamRepositoryImpl extends GenericRepositoryImpl<GrantProgramParam, Long> implements GrantProgramParamRepository {

    @Override
    public List<GrantProgramParam> findByGrantProgramInDate(Long grantProgramId, String paramTypeId, Date date){
        TypedQuery<GrantProgramParam> query = entityManager.createNamedQuery(GrantProgramParam.FIND_BY_GRANT_PROGRAM_IN_DATE, GrantProgramParam.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("paramTypeId", paramTypeId);
        query.setParameter("date", date);
        return query.getResultList();
    }



}
