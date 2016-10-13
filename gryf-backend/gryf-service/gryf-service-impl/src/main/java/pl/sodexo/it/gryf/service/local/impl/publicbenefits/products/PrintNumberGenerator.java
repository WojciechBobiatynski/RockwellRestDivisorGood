package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.products.PrintNumberChecksumProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Klasa generuje numery drukowane dla bonów
 *
 * Created by jbentyn on 2016-10-12.
 */
@Component
public class PrintNumberGenerator {

    private static final Random RANDOM = new Random();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMAT_PADDING_ONE = "%01d";
    private static final String FORMAT_PADDING_TWO = "%02d";
    private static final String FORMAT_PADDING_SIX = "%06d";
    private static final String FORMAT_PADDING_NINE = "%09d";

    @Autowired
    private PrintNumberChecksumProvider printNumberChecksumProvider;

    String generate(PrintNumberDto printNumberDto) {
        StringBuilder builder = new StringBuilder();
        builder.append(getCountryNumberFormatted(printNumberDto))
                .append(getConstantFormatted(printNumberDto))
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

    private String getCountryNumberFormatted(PrintNumberDto printNumberDto) {
        return String.format(FORMAT_PADDING_ONE, printNumberDto.getCountryNumber());
    }

    private String getConstantFormatted(PrintNumberDto printNumberDto) {
        return String.format(FORMAT_PADDING_ONE, printNumberDto.getConstant());
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
