package pl.sodexo.it.gryf.service.batch.printNumber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;

/**
 * Processsor joba batchowego dla generacji numerów drukowanych bonów
 *
 * Created by jbentyn on 2016-10-14.
 */
@Slf4j
public class PrintNumberProcessor implements ItemProcessor<PrintNumberDto,PrintNumberResultDto> {

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Override
    public PrintNumberResultDto process(PrintNumberDto printNumberDto) throws Exception {
        LOGGER.info("Process...LoggedUser = {}", GryfUser.getLoggedUser());
        return printNumberGenerator.generate(printNumberDto);
    }
}
