package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductInstanceSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ProductInstanceSearchMapper;

/**
 * Created by jbentyn on 2016-10-17.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ProductInstanceSearchDaoImpl implements ProductInstanceSearchDao {

    @Autowired
    private ProductInstanceSearchMapper productInstanceSearchMapper;

    @Override
    public Long countAvailableToNumberGeneration(String productId) {
        return productInstanceSearchMapper.countAvailableToNumberGeneration(productId);
    }
}
