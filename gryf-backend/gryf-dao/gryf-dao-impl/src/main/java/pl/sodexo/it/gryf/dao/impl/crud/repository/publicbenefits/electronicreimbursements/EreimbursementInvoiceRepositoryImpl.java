package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.electronicreimbursements;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementInvoiceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementInvoice;

/**
 * Created by Isolution on 2016-12-20.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EreimbursementInvoiceRepositoryImpl extends GenericRepositoryImpl<EreimbursementInvoice, Long> implements EreimbursementInvoiceRepository {

}
