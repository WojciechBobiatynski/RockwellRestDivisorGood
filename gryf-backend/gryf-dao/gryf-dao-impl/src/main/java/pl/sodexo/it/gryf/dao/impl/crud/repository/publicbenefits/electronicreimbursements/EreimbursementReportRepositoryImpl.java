package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementReportRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementReport;

/**
 * Implementacja repozytorium dla raportów rozliczeń elektronicznych
 *
 * Created by akmiecinski on 2016-12-21.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementReportRepositoryImpl extends GenericRepositoryImpl<EreimbursementReport, Long> implements EreimbursementReportRepository {

}
