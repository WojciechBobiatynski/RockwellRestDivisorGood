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
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportParamsDTO;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobStatus;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;
import pl.sodexo.it.gryf.service.api.programs.GrantProgramService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-12-05.
 */
@Service("asynchJobImportService")
public class AsynchJobImportServceImpl implements AsynchJobService{

    //STATIC FIELDS

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBaseDataServiceImpl.class);

    //PRIVATE FILEDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FileService fileService;

    @Autowired
    private GrantProgramService grantProgramService;

    //PUBLIC METHODS

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsynchronizeJobResultInfoDTO processAsynchronizeJob(AsynchronizeJobInfoDTO dto){
        try {
            int allRows;
            int normalSuccessRows = 0;
            int normalBussinssRows = 0;
            int normalErrorRows = 0;
            int extraSuccessRows = 0;
            int extraBussinssRows = 0;
            int extraErrorRows = 0;

            ImportParamsDTO paramsDTO = createImportParamDTO(dto.getParams());


            //GET SERVICE - Wybierz Implementacje uslugi na podstawie typu zadania
            ImportDataService importDataService = (ImportDataService) BeanUtils.findBean(context, dto.getTypeParams());


            //OPEN FILE
            InputStream is = fileService.getInputStream(paramsDTO.getPath());

            //ITERATORS
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            //SAVE EMPTY ROWS
            allRows = importDataService.saveEmptyRows(dto.getId(), sheet.getPhysicalNumberOfRows() - 1);

            //ZAPIS PODSTAWOWYCH WIERSZY
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum() != 0){
                    try {

                        //UPDATE RECORD INFO
                        importDataService.saveImportDataRowBeforeSaveData(dto.getId(), paramsDTO, row);

                        //SAVE DATA
                        importDataService.saveData(dto.getId(), paramsDTO, row);
                        normalSuccessRows++;

                        //BLEDY BIZNESOWE
                    }catch(EntityValidationException e){
                        normalBussinssRows++;
                        importDataService.saveEntityValidationError(dto.getId(), row, e);

                        //BLEDY KRYTYCZNE
                    }catch(RuntimeException e){
                        normalErrorRows++;
                        LOGGER.error(String.format("Nieoczekiwane b????d podczas importu wiersza numer [%s] "
                                + "dla zlecenia importu [%s]", row.getRowNum(), dto.getId()), e);
                        importDataService.saveRuntimeError(dto.getId(), row, e);
                    }
                }
            }

            //ZAPIS DODATKOWYCH WIERSZY
            List<Long> extraRowIdList = importDataService.getExtraRows(dto.getId());
            for(Long extraRowId : extraRowIdList){
                try {
                    importDataService.saveExtraRow(extraRowId, paramsDTO);
                    extraSuccessRows++;
                }catch(EntityValidationException e){
                        extraBussinssRows++;
                        importDataService.saveEntityValidationError(extraRowId, e);

                    //BLEDY KRYTYCZNE
                }catch(RuntimeException e){
                    extraErrorRows++;
                    LOGGER.error(String.format("Nieoczekiwane b????d podczas dodatkowego zadania (indetyfikator wiersza = %s)"
                            + "dla zlecenia importu [%s]", extraRowId, dto.getId()), e);
                    importDataService.saveRuntimeError(extraRowId, e);
                }
            }

            AsynchronizeJobResultInfoDTO resultDTO = new AsynchronizeJobResultInfoDTO(dto.getId());
            resultDTO.setStatus((allRows == (normalSuccessRows + extraSuccessRows)) ? AsynchronizeJobStatus.S.name() : AsynchronizeJobStatus.C.name());
            resultDTO.setDescription(importDataService.createDescription(allRows, normalSuccessRows, normalBussinssRows, normalErrorRows,
                                                                                    extraSuccessRows, extraBussinssRows, extraErrorRows));
            return resultDTO;

        } catch (IOException e) {
            throw new RuntimeException("Wystapi?? b??ad przy otwarciu pliku do importu", e);
        }
    }

    private void fillGrantProgram(ImportParamsDTO paramsDTO) {
        if (Objects.nonNull(paramsDTO)) {
            GrantProgramDictionaryDTO grantProgramById = grantProgramService.getGrantProgramById(paramsDTO.getGrantProgramId());
            paramsDTO.setGrantProgram(grantProgramById);
        }
    }

    //PRIVATE METHODS

   private ImportParamsDTO createImportParamDTO(String params){
       ImportParamsDTO result = new ImportParamsDTO();

       String[] tabParams = params.split(";");
       if(tabParams.length != 2){
           throw new RuntimeException("Parametry dla importy plik??w musz?? by?? w formacie: "
                   + "identyfikator_programu_dofinansowania;scie??ka_do_pliku'");
       }

       try {
           result.setGrantProgramId(Long.parseLong(tabParams[0]));
       }catch(NumberFormatException e) {
           throw new RuntimeException(String.format("Nie uda??o si?? odczyta?? idnetyfikatora programu "
                                            + "dofinansowania z warto??ci [%s]", tabParams[0]));
       }
       result.setPath(tabParams[1]);

       fillGrantProgram(result);

       return result;
   }



}
