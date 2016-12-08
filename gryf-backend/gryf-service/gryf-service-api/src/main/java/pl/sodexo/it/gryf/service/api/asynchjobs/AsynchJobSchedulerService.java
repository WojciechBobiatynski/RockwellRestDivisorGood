package pl.sodexo.it.gryf.service.api.asynchjobs;

import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobResultInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Isolution on 2016-12-02.
 */
public interface AsynchJobSchedulerService {

    Long saveAsynchronizeJob(AsynchJobDetailsDTO createDTO);

    void processAsynchronizeJob();

    Long reserveAsynchronizeJob();

    AsynchronizeJobInfoDTO getAsynchronizeJobInfoDTO(Long jobId);

    void successEndJob(AsynchronizeJobResultInfoDTO resultDTO);

    void saveBussinesError(Long jobId, EntityValidationException e);

    void saveRuntimeError(Long jobId, RuntimeException e);

    List<AsynchJobSearchResultDTO> findAsynchJobs(AsynchJobSearchQueryDTO queryDTO);

    AsynchJobDetailsDTO findAsynchJobDetails(Long jobId);

    List<ImportDataRowSearchResultDTO> findImportDataRows(ImportDataRowSearchQueryDTO queryDTO);

    Map<String, String> getJobStatuses();

    Map<String, String> getJobTypes(Optional<Long> grantProgramId, boolean onlyImportJobs);

    Map<String, String> getImportDataRowsStatuses();

}
