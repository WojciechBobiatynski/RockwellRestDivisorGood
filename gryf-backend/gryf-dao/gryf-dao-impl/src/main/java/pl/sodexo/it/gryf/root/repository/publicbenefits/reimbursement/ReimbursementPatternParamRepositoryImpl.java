package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-03.
 */
@Repository
public class ReimbursementPatternParamRepositoryImpl extends GenericRepositoryImpl<ReimbursementPatternParam, Long> implements ReimbursementPatternParamRepository {

    @Override
    public List<ReimbursementPatternParam> findByReimbursementPatternParamInDate(Long reimbursementPatternId, String paramId, Date date){
        TypedQuery<ReimbursementPatternParam> query = entityManager.createNamedQuery(ReimbursementPatternParam.FIND_BY_REIMBURSEMENT_PATTERN_PARAM_IN_DATE, ReimbursementPatternParam.class);
        query.setParameter("reimbursementPatternId", reimbursementPatternId);
        query.setParameter("paramId", paramId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
