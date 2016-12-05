package pl.sodexo.it.gryf.dao.api.crud.repository.importdata;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;

/**
 * Created by Isolution on 2016-12-01.
 */
public interface ImportDataRowRepository extends GenericRepository<ImportDataRow, Long> {

    ImportDataRow getByImportJobAndRowNum(Long importJobId, Integer rowNum);
}
