package pl.sodexo.it.gryf.service.batch.printNumber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;

/**
 * Processsor joba batchowego dla generacji numerów drukowanych bonów
 *
 * Created by jbentyn on 2016-10-14.
 */
@Slf4j
public class PrintNumberProcessor implements ItemProcessor<PrintNumberDto,PrintNumberDto> {

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Override
    public PrintNumberDto process(PrintNumberDto printNumberDto) throws Exception {
        printNumberDto.setGeneratedPrintNumber(printNumberGenerator.generate(printNumberDto));
        //TODO w celach testowych
        try {
            LOGGER.info("Start process");
            Thread.sleep(10_000L);
            LOGGER.info("LoggedUser = {}", GryfUser.getLoggedUser());
            LOGGER.info("Stop process");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return printNumberDto;
    }
}
