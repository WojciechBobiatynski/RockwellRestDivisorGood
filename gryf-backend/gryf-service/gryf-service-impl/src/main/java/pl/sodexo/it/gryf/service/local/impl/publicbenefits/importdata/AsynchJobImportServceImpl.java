package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobResultInfoDTO;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobStatus;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Isolution on 2016-12-05.
 */
@Service("asynchJobImportServce")
public class AsynchJobImportServceImpl implements AsynchJobService{

    //STATIC FIELDS

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBaseDataServiceImpl.class);

    //PRIVATE FILEDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FileService fileService;

    //PUBLIC METHODS

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsynchronizeJobResultInfoDTO processAsynchronizeJob(AsynchronizeJobInfoDTO dto){
        try {
            int allRows;
            int successRows = 0;
            int bussinssRows = 0;
            int errorRows = 0;

            //GET SERVICE
            ImportDataService importDataService = (ImportDataService) BeanUtils.findBean(context, dto.getTypeParams());

            //OPEN FILE
            InputStream is = fileService.getInputStream(dto.getParams());

            //ITERATORS
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            //SAVE EMPTY ROWS
            allRows = sheet.getPhysicalNumberOfRows() - 1;
            importDataService.saveEmptyRows(dto.getId(), allRows);

            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum() != 0){
                    try {
                        //SAVE DATA
                        importDataService.saveData(dto.getId(), row);
                        successRows++;

                        //BLEDY BIZNESOWE
                    }catch(EntityValidationException e){
                        bussinssRows++;
                        importDataService.saveEntityValidationError(dto.getId(), row, e);

                        //BLEDY KRYTYCZNE
                    }catch(RuntimeException e){
                        errorRows++;
                        LOGGER.error(String.format("Nieoczekiwane błąd podczas importu wiersza numer [%s] "
                                + "dla zlecenia importu [%s]", row.getRowNum(), dto.getId()), e);
                        importDataService.saveRuntimeError(dto.getId(), row, e);
                    }
                }
            }
            AsynchronizeJobResultInfoDTO resultDTO = new AsynchronizeJobResultInfoDTO(dto.getId());
            resultDTO.setStatus((allRows == successRows) ? AsynchronizeJobStatus.S.name() : AsynchronizeJobStatus.C.name());
            resultDTO.setDescription(createDescription(allRows, successRows, bussinssRows, errorRows));
            return resultDTO;

        } catch (IOException e) {
            throw new RuntimeException("Wystapił bład przy otwarciu pliku do importu", e);
        }
    }

    private String createDescription(int allRows, int successRows, int bussinssRows, int errorRows){
        if(allRows == successRows){
            return String.format("Wczytano wszystkie wiersze. Ilość wczytanych wierszy: %s.", successRows);
        }
        return String.format("Wczytano częściowo wiersze. Ilość wszystkich wierszy: %s, ilość wierszy poprawnie wczytanych: %s, "
                    + "ilość wierszy błędnych (biznesowe): %s, ilość wierszy błednych (krytyczne): %s.", allRows, successRows, bussinssRows, errorRows);
    }


}
