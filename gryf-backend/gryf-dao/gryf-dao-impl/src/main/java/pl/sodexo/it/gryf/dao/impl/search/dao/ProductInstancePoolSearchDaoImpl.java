package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductInstancePoolSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ProductInstancePoolSearchMapper;

import java.util.List;

/**
 * Created by akmiecinski on 2016-12-22.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ProductInstancePoolSearchDaoImpl implements ProductInstancePoolSearchDao {

    @Autowired
    private ProductInstancePoolSearchMapper productInstancePoolSearchMapper;

    @Override
    public List<PbeProductInstancePoolDto> findExpiredPoolInstances() {
        return productInstancePoolSearchMapper.findExpiredPoolInstances(new UserCriteria());
    }
}
