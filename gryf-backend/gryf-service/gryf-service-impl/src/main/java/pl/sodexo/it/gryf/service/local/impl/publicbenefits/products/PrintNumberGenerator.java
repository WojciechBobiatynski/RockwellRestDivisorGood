package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.products.PrintNumberChecksumProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa generuje numery drukowane dla bonów
 *
 * Created by jbentyn on 2016-10-12.
 */
@Component
@Slf4j
public class PrintNumberGenerator {

    private static final Random RANDOM = new Random();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMAT_PADDING_ONE = "%01d";
    private static final String FORMAT_PADDING_TWO = "%02d";
    private static final String FORMAT_PADDING_SIX = "%06d";
    private static final String FORMAT_PADDING_NINE = "%09d";

    @Autowired
    private PrintNumberChecksumProvider printNumberChecksumProvider;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Async
    public void generateForProduct(String productNumber) {
        //TODO załaduj porcję bonów do wygenerowania numerów
        List<PrintNumberDto> printNumbersChunk = new ArrayList<>();
        generate(printNumbersChunk);
        //TODO powiadomienie mailem o zakończeniu generacji?
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void generate(List<PrintNumberDto> printNumberDtoList) {
        printNumberDtoList.forEach(dto -> dto.setGeneratedPrintNumber(generate(dto)));
        //TODO update do bazy

        //TODO w celach testowych
        try {
            LOGGER.info("Start waiting");
            Thread.sleep(10_000L);
            LOGGER.info("Stop waiting");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String generate(PrintNumberDto printNumberDto) {
        StringBuilder builder = new StringBuilder();
        builder.append(applicationParameters.getPrintNumberCountryCodePoland())
                .append(getTypeNumberFormatted(printNumberDto))
                .append(getFaceValueFormatted(printNumberDto))
                .append(getValidDateFormatted(printNumberDto))
                .append(getChecksumFormatted(printNumberDto))
                .append(getRandomFormatted())
                .append(getProductInstanceNumberFormatted(printNumberDto));
        return builder.toString();
    }

    private String getChecksumFormatted(PrintNumberDto printNumberDto) {
        Integer checksum = printNumberChecksumProvider.generateChecksum(printNumberDto);
        return String.format(FORMAT_PADDING_TWO, checksum);
    }

    private String getRandomFormatted() {
        return String.format(FORMAT_PADDING_TWO, RANDOM.nextInt(100));
    }

    private String getTypeNumberFormatted(PrintNumberDto printNumberDto) {
        return String.format(FORMAT_PADDING_TWO, printNumberDto.getTypeNumber());
    }

    private String getFaceValueFormatted(PrintNumberDto printNumberDto) {
        return String.format(FORMAT_PADDING_SIX, printNumberDto.getFaceValue());
    }

    private String getValidDateFormatted(PrintNumberDto printNumberDto) {
        String formattedDate = DATE_FORMAT.format(printNumberDto.getValidDate());
        return formattedDate.substring(1);
    }

    private String getProductInstanceNumberFormatted(PrintNumberDto printNumberDto) {
        return String.format(FORMAT_PADDING_NINE, printNumberDto.getProductInstanceNumber());
    }

}
