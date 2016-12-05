package pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Row;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;

/**
 * Created by Isolution on 2016-11-30.
 */
public interface ImportDataService {

    void saveEmptyRows(Long importJobId, int rowNums);

    void saveData(Long importJobId, Row row);

    void saveEntityValidationError(Long importJobId, Row row, EntityValidationException e);

    void saveRuntimeError(Long importJobId, Row row, RuntimeException e);

}
