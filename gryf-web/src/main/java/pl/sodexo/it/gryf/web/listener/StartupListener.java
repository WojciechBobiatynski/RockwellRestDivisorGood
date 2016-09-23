package pl.sodexo.it.gryf.web.listener;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.GryfInternalException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Listener startu aplikacji.
 * 
 * Created by akuchna on 2016-09-22.
 */
@Component
public class StartupListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);

    private static final String PROPERTIES_FILE = "appInfo.properties";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String APP_VERSION_UNAVAILABLE = "Wersja niedostępna";
    
    @PostConstruct
    private void onStart() { // NOSONAR
        try {
            LOGGER.info("Inicjalizacja aplikacji");
            
            LOGGER.info("...");
            LOGGER.info("[APP VERSION] {}", getAppVersion());
            
            LOGGER.info("Zainicjowano aplikacje");
        } catch (Throwable t) {
            LOGGER.error("Blad podczas inicjalizacji", t);
            Throwables.propagate(t);
        }
    }

    private String getAppVersion() {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (stream == null) {
            return APP_VERSION_UNAVAILABLE;
        }

        Properties properties = new Properties();
        try {
            properties.load(stream);
            return properties.getProperty(PROPERTY_APP_VERSION);
        } catch (IOException e) {
            LOGGER.error("Pobieranie wersji aplikacji zakończone niepowodzeniem.");
            throw new GryfInternalException("Blad podczas pobierania wersji aplikacji", e);
        }
    }
}
