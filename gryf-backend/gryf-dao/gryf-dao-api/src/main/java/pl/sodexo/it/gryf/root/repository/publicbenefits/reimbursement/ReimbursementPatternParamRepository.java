package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementPatternParamRepository extends GenericRepository<ReimbursementPatternParam, Long> {

    List<ReimbursementPatternParam> findByReimbursementPatternParamInDate(Long reimbursementPatternId, String paramId, Date date);
}
