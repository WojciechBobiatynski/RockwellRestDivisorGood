package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-03.
 */
@Repository
public class ReimbursementPatternRepository extends GenericRepository<ReimbursementPattern, Long>{

    public List<ReimbursementPattern> findPatternsByDate(Date date){
        TypedQuery<ReimbursementPattern> query = entityManager.createNamedQuery(ReimbursementPattern.FIND_BY_DATE, ReimbursementPattern.class);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
