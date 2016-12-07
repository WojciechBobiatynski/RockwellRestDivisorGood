package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;

import java.util.List;

public interface AsynchJobSearchMapper {

    List<AsynchJobSearchResultDTO> findAsynchronusJobs(@Param("criteria") UserCriteria criteria, @Param("searchParams") AsynchJobSearchQueryDTO searchParams);
}
