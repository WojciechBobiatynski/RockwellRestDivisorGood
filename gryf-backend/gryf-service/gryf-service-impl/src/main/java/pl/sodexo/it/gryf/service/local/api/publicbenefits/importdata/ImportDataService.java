package pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Row;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;

import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
public interface ImportDataService {

    int saveEmptyRows(Long importJobId, int rowNums);

    void saveData(Long importJobId, ImportParamsDTO paramsDTO, Row row);

    //SAVE EXTRA ROWS

    List<Long> getExtraRows(Long importJobId);

    void saveExtraRow(Long extraRowId, ImportParamsDTO paramsDTO);

    //ERRORS METHODS

    void saveRuntimeError(Long importJobId, Row row, RuntimeException e);

    void saveRuntimeError(Long rowId, RuntimeException e);

    void saveEntityValidationError(Long importJobId, Row row, EntityValidationException e);

    void saveEntityValidationError(Long rowId, EntityValidationException e);

    String createDescription(int allRows, int normalSuccessRows, int normalBussinssRows, int NormalErrorRows,
                                            int extraSuccessRows, int extraBussinssRows, int extraErrorRows);
}
