package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;

import java.util.List;

public interface AsynchJobSearchDao {

    List<AsynchJobSearchResultDTO> findAsynchronusJobs(AsynchJobSearchQueryDTO dto);
}
