package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementPatternRepository extends GenericRepository<ReimbursementPattern, Long> {

    List<ReimbursementPattern> findPatternsByDate(Date date);
}
