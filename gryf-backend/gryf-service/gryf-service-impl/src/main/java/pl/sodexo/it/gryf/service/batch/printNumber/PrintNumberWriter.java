package pl.sodexo.it.gryf.service.batch.printNumber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;

import java.util.List;

/**
 * Writer dla procesu batchowego generowania numerów drukowanych bonów
 *
 * Created by jbentyn on 2016-10-14.
 */
@Slf4j
public class PrintNumberWriter implements ItemWriter<PrintNumberDto>{

    @Override
    public void write(List<? extends PrintNumberDto> list) throws Exception {
        list.forEach( dto -> LOGGER.info("writing dto... dto={}",dto));
    }
}
