package pl.sodexo.it.gryf.service.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.service.api.publicbenefits.products.ProductInstanceService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;

/**
 * Created by jbentyn on 2016-10-12.
 */
@Service
public class ProductInstanceServiceImpl implements ProductInstanceService {

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Override
    public void generatePrintNumbersForProduct(String productNumber) {

        //TODO sprawdzenie czy sa bony które trzeba przeprocesować
        printNumberGenerator.generateForProduct(productNumber);
    }
}
