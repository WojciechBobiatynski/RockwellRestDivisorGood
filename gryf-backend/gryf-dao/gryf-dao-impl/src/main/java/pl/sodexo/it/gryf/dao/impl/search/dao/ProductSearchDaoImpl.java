package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.ProductHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ProductSearchMapper;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-17.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ProductSearchDaoImpl implements ProductSearchDao {

    @Autowired
    private ProductSearchMapper productSearchMapper;

    @Override
    public List<ProductHeadDto> findProductsByTrainingInstanceId(Long trainingInstanceId) {
        return productSearchMapper.findProductsByTrainingInstanceId(new UserCriteria(), trainingInstanceId);
    }
}
