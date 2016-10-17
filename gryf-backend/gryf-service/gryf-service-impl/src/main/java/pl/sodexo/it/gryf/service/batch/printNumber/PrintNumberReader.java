package pl.sodexo.it.gryf.service.batch.printNumber;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;

import java.util.Date;

/**
 * Reader dla procesu batchowego generowania numerów drukowanych bonów
 *
 * Created by jbentyn on 2016-10-14.
 */
public class PrintNumberReader implements ItemReader<PrintNumberDto> {

   private  int count;

    @Override
    public PrintNumberDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        PrintNumberDto dto = new PrintNumberDto();
        dto.setFaceValue(1500);
        dto.setProductInstanceNumber(1);
        dto.setTypeNumber(30);
        dto.setValidDate(new Date());
        count ++;

        return count> 10? null: dto;
    }

}
