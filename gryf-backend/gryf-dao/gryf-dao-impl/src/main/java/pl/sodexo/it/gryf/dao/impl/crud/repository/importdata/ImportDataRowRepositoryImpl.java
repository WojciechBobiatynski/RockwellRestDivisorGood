package pl.sodexo.it.gryf.dao.impl.crud.repository.importdata;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-12-01.
 */
@Repository
public class ImportDataRowRepositoryImpl extends GenericRepositoryImpl<ImportDataRow, Long> implements ImportDataRowRepository {

    @Override
    public ImportDataRow getByImportJobAndRowNum(Long importJobId, Integer rowNum){
        TypedQuery<ImportDataRow> query = entityManager.createNamedQuery("ImportDataRow.getByImportJobAndRowNum", ImportDataRow.class);
        query.setParameter("importJobId", importJobId);
        query.setParameter("rowNum", rowNum);

        List<ImportDataRow> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
