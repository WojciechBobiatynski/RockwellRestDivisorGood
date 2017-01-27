package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.invoices;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.invoices.InvoiceArchiveRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.InvoiceArchive;

/**
 * Created by Isolution on 2017-01-27.
 */
@Repository
public class InvoiceArchiveRepositoryImpl extends GenericRepositoryImpl<InvoiceArchive, Long> implements InvoiceArchiveRepository {

}
