package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchResultDTO;

import java.util.List;

public interface AsynchJobSearchDao {

    List<AsynchJobSearchResultDTO> findAsynchJobs(AsynchJobSearchQueryDTO dto);

    AsynchJobDetailsDTO findAsynchJobDetails(Long jobId);

    List<ImportDataRowSearchResultDTO> findImportDataRows(ImportDataRowSearchQueryDTO queryDTO);
}
