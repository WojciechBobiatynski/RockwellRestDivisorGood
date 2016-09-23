package pl.sodexo.it.gryf.common.logging.eclipselink;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adziobek on 22.09.2016.
 */
public class LogbackSessionLog extends AbstractSessionLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackSessionLog.class);

    private Map<String, Integer> categoryLogLevelMap;

    public LogbackSessionLog() {
        this.categoryLogLevelMap = new HashMap();
        for (int i = 0; i < loggerCatagories.length; ++i) {
            String loggerCategory = loggerCatagories[i];
            this.categoryLogLevelMap.put(loggerCategory, null);
        }
    }

    public void setLevel(int level, String category) {
        if (category == null) {
            this.level = level;
        } else if (this.categoryLogLevelMap.containsKey(category)) {
            this.categoryLogLevelMap.put(category, Integer.valueOf(level));
        }
    }

    public int getLevel(String category) {
        if (category != null) {
            Integer level = (Integer) this.categoryLogLevelMap.get(category);
            if (level != null) {
                return level.intValue();
            }
        }
        return super.getLevel(category);
    }

    public boolean shouldLog(int level, String category) {
        return this.getLevel(category) <= level;
    }

    protected void initialize() {
        this.setShouldPrintSession(true);
        this.setShouldPrintConnection(true);
    }

    public void log(SessionLogEntry entry) {
        if (shouldLog(entry)) {
            synchronized (this) {
                if (entry.hasMessage()) {
                    logMessage(entry);
                }
                if (entry.hasException()) {
                    logException(entry);
                }
            }
        }
    }

    private boolean shouldLog(SessionLogEntry entry) {
        return this.shouldLog(entry.getLevel(), entry.getNameSpace());
    }

    private void logMessage(SessionLogEntry entry) {
        logWithProperLevel(entry.getLevel(), this.formatMessage(entry).replace("\r\n\t", " | "));
    }

    private void logException(SessionLogEntry entry) {
        if (this.shouldLogExceptionStackTrace()) {
            entry.getException().printStackTrace(new PrintWriter(this.getWriter()));
        } else {
            LOGGER.error(entry.getException().toString(), entry.getException());
        }
    }

    private void logWithProperLevel(int level, String message) {
        switch (level) {
            case 0:
            case 1:
            case 2:
                LOGGER.trace(message);
                break;
            case 3:
            case 4:
                LOGGER.debug(message);
                break;
            case 5:
                LOGGER.info(message);
                break;
            case 6:
                LOGGER.warn(message);
                break;
            case 7:
                LOGGER.error(message);
                break;
            case 8:
                break;
            default:
                break;
        }
    }
}
