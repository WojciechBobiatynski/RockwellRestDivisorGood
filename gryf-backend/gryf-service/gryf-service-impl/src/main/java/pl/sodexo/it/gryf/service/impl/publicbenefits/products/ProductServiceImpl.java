package pl.sodexo.it.gryf.service.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductSearchDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.products.ProductService;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-17.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductSearchDao productSearchDao;

    @Override
    public List<ProductDto> findProducts() {
        return productSearchDao.findProducts();
    }
}
