package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementLine;

import java.util.List;

/**
 * Repozytorium dla częściowych rozliczeń bonów elektronicznych
 * <p>
 * Created by dptaszynski 2020-07-06;
 */
public interface EreimbursementLineRepository extends GenericRepository<EreimbursementLine, Long> {

    Auditable getAuditableInfoById(Long ereimbursementLineId);

    List<EreimbursementLine> getListByEreimbursement(Ereimbursement ereimbursement);

    int deleteListByEreimbursement(Ereimbursement ereimbursement);
}
