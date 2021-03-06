package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantprograms;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramLimitRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class GrantProgramLimitRepositoryImpl extends GenericRepositoryImpl<GrantProgramLimit, Long> implements GrantProgramLimitRepository {

    @Override
    public List<GrantProgramLimit> findByGrantProgramEntSizeLimitTypeInDate(Long grantProgramId, String enterpriseSizeId, GrantProgramLimit.LimitType limitType, Date date){
        TypedQuery<GrantProgramLimit> query = entityManager.createNamedQuery("GrantProgramLimit.findByGrantProgramEntSizeLimitTypeInDate", GrantProgramLimit.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("enterpriseSizeId", enterpriseSizeId);
        query.setParameter("limitType", limitType);
        query.setParameter("date", date);
        return query.getResultList();
    }



}
