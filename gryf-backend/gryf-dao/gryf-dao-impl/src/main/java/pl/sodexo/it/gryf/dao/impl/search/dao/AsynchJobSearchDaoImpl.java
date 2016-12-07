package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.search.dao.AsynchJobSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.AsynchJobSearchMapper;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AsynchJobSearchDaoImpl implements AsynchJobSearchDao {

    @Autowired
    private AsynchJobSearchMapper asynchJobSearchMapper;

    @Override
    public List<AsynchJobSearchResultDTO> findAsynchronusJobs(AsynchJobSearchQueryDTO searchQueryDTO) {
        return asynchJobSearchMapper.findAsynchronusJobs(new UserCriteria(), searchQueryDTO);
    }
}
