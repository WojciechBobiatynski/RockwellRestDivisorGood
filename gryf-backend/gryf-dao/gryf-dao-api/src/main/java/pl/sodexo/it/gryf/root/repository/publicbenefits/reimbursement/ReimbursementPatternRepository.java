package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementPatternRepository extends GenericRepository<ReimbursementPattern, Long> {

    List<ReimbursementPattern> findPatternsByDate(Date date);
}
