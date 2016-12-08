package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchResultDTO;

import java.util.List;

public interface AsynchJobSearchMapper {

    List<AsynchJobSearchResultDTO> findAsynchJobs(@Param("criteria") UserCriteria criteria, @Param("searchParams") AsynchJobSearchQueryDTO searchParams);

    AsynchJobDetailsDTO findAsynchJobDetails(@Param("criteria") UserCriteria criteria, @Param("jobId") Long jobId);

    List<ImportDataRowSearchResultDTO> findImportDataRows(@Param("criteria") UserCriteria criteria, @Param("searchParams") ImportDataRowSearchQueryDTO searchParams);

}
