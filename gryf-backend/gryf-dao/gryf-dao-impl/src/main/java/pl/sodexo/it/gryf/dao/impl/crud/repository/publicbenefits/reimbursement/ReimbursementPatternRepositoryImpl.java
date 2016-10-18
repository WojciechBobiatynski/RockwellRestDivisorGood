package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementPatternRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-03.
 */
@Repository
public class ReimbursementPatternRepositoryImpl extends GenericRepositoryImpl<ReimbursementPattern, Long> implements ReimbursementPatternRepository {

    @Override
    public List<ReimbursementPattern> findPatternsByDate(Date date){
        TypedQuery<ReimbursementPattern> query = entityManager.createNamedQuery(ReimbursementPattern.FIND_BY_DATE, ReimbursementPattern.class);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
