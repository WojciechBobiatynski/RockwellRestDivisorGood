package pl.sodexo.it.gryf.service.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.service.api.publicbenefits.products.ProductInstanceService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;
import pl.sodexo.it.gryf.service.utils.AsyncUtil;

import java.util.concurrent.Executor;

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
        startGeneratingNumbersAsync(productNumber);
    }

    private void startGeneratingNumbersAsync(String productNumber) {
        Executor executor = AsyncUtil.getDelegatingSecurityContextAsyncExecutor();
        executor.execute(() -> printNumberGenerator.generateForProduct(productNumber));
    }
}
