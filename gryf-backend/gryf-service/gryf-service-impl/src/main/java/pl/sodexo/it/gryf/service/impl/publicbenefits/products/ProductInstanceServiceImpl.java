package pl.sodexo.it.gryf.service.impl.publicbenefits.products;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("printNumberJob")
    private Job job;

    @Override
    public void generatePrintNumbersForProduct(String productNumber) {
        //TODO sprawdzenie czy sa bony które trzeba przeprocesować
          startGeneratingNumbersAsync(productNumber);
    }

    private void startGeneratingNumbersAsync(String productNumber)  {
        Executor executor = AsyncUtil.getDelegatingSecurityContextAsyncExecutor();
        executor.execute(() -> {
            try {
                JobParameters jobParameters = new JobParameters();
                JobExecution execution = jobLauncher.run(job, jobParameters);
                //TODO powiadomienie mailem o zakończeniu generacji?

                //TODO obsługa wyjątków
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            }
        });

    }

}
