package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.products.PrintNumberChecksumProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test dla klasy PrintNumberGenerator
 * Created by jbentyn on 2016-10-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PrintNumberGeneratorTest {

    @Configuration
    static class PrintNumberGeneratorTestConfiguration {

        @Bean
        public PrintNumberGenerator printNumberGenerator() {
            return new PrintNumberGenerator();
        }

        @Bean
        public PrintNumberChecksumProvider printNumberChecksumProvider() {
            return new DummyNumberChecksumProvider();
        }

        @Bean
        public ApplicationParameters applicationParameters() {
            ApplicationParameters applicationParameters = mock(ApplicationParameters.class);
            when(applicationParameters.getPrintNumberCountryCodePoland()).thenReturn("31");
            return applicationParameters;
        }
    }

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Test
    public void generate() throws Exception {
        PrintNumberDto dto = new PrintNumberDto();
        dto.setFaceValue(1500);
        dto.setProductInstanceNumber(1);
        dto.setTypeNumber(30);
        dto.setValidDate(asDate("2016-10-12"));

        String generatedNumber = printNumberGenerator.generate(dto);

        assertEquals("Wygenerowany numer bonu musi miec długość 30", 30, generatedNumber.length());

        String pattern = "31300015000161012\\d{4}000000001";
        assertTrue("Wygenerowany numer musi zgadzać się z numerem założonym z dokładnością do liczby losowej i CRC :", generatedNumber.matches(pattern));

    }

    private Date asDate(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Blad parsowania, dateString=" + dateString);
        }
    }
}