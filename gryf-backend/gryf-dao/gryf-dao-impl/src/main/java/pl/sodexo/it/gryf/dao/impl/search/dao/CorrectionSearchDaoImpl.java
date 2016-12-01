package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.dao.api.search.dao.CorrectionSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.CorrectionSearchMapper;

/**
 * Implementacja dao do operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class CorrectionSearchDaoImpl implements CorrectionSearchDao{

    @Autowired
    private CorrectionSearchMapper correctionSearchMapper;

    @Override
    public Integer findCorrectionsNumberByErmbsId(Long ermbsId) {
        return correctionSearchMapper.findCorrectionsNumberByErmbsId(new UserCriteria(), ermbsId);
    }
}
