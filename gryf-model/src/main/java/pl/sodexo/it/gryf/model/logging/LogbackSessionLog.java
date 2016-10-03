package pl.sodexo.it.gryf.model.logging;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa przekazująca logi generowane przez Eclipselinka do Logbacka
 *
 * Created by adziobek on 22.09.2016.
 */
public class LogbackSessionLog extends AbstractSessionLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackSessionLog.class);

    private Map<String, Integer> categoryLogLevelMap;

    public LogbackSessionLog() {
        this.categoryLogLevelMap = new HashMap<>();
        for (String loggerCategory : loggerCatagories) {
            this.categoryLogLevelMap.put(loggerCategory, null);
        }
    }

    @Override
    public void setLevel(int level, String category) {
        if (category == null) {
            this.level = level;
        } else {
            assignLevelToCategory(level, category);
        }
    }

    private void assignLevelToCategory(int level, String category) {
        if (!this.categoryLogLevelMap.containsKey(category)) return;
        this.categoryLogLevelMap.put(category, level);
    }

    @Override
    public int getLevel(String category) {
        if (category == null) return super.getLevel(null);

        Integer level = this.categoryLogLevelMap.get(category);
        if (level != null) return level;
        return super.getLevel(category);
    }

    @Override
    public boolean shouldLog(int level, String category) {
        return this.getLevel(category) <= level;
    }

    @Override
    public void log(SessionLogEntry entry) {
        if (!shouldLog(entry)) {
            return;
        }
        synchronized (this) {
            if (entry.hasMessage()) logMessage(entry);
            if (entry.hasException()) logException(entry);
        }
    }

    private boolean shouldLog(SessionLogEntry entry) {
        return this.shouldLog(entry.getLevel(), entry.getNameSpace());
    }

    private void logMessage(SessionLogEntry entry) {
        logWithProperLevel(entry.getLevel(), this.formatMessage(entry).replace("\r\n\t", " | "));
    }

    private void logException(SessionLogEntry entry) {
        Throwable ex = entry.getException();
        if (this.shouldLogExceptionStackTrace()) {
            LOGGER.error(ex.getMessage(), ex);
        } else {
            LOGGER.error(ex.getMessage());
        }
    }

    private void logWithProperLevel(int level, String message) {
        switch (level) {
            case ALL:
            case FINEST:
            case FINER:
                LOGGER.trace(message);
                break;
            case FINE:
            case CONFIG:
                LOGGER.debug(message);
                break;
            case INFO:
                LOGGER.info(message);
                break;
            case WARNING:
                LOGGER.warn(message);
                break;
            case SEVERE:
                LOGGER.error(message);
                break;
            default:
                break;
        }
    }
}
