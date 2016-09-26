package pl.sodexo.it.gryf.common.logging;

/**
 * Enum z definicją poziomów logowania.
 *
 * NONE - brak logowania
 * DEFAULT - tylko wywołanie funkcji
 * TRACE - wywołanie funkcji + toString() wszystkich argumentów
 *
 * Created by adziobek on 26.09.2016.
 */
public enum LoggingLevel {

    NONE, DEFAULT, TRACE
}
