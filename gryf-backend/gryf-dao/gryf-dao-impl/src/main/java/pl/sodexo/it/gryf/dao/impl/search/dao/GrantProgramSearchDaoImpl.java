package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.dao.api.search.dao.GrantProgramSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.GrantProgramSearchMapper;

/**
 * Dao dla operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class GrantProgramSearchDaoImpl implements GrantProgramSearchDao {

    @Autowired
    private GrantProgramSearchMapper grantProgramSearchMapper;

    @Override
    public Long findGrantProgramIdByTrainingInstanceId(Long trainingInstanceId) {
        return grantProgramSearchMapper.findGrantProgramIdByTrainingInstanceId(new UserCriteria(), trainingInstanceId);
    }
}
