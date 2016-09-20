package pl.sodexo.it.gryf.root.repository;

import pl.sodexo.it.gryf.model.DayType;

import java.util.Date;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GryfPLSQLRepository {

    Date getNextBusinessDay(Date date);

    /**
     * Funkcja zwraca nty dzień roboczy
     * @param date data początkowa
     * @param days ilość dni roboczych będzie dodana do daty początkowej
     * @return nty dzień roboczy (data)
     */
    Date getNthBusinessDay(Date date, Integer days);

    /**
     * Funkcja zwraca nty dzień zadanego typu, B - roboczy, C - kalendarzowy
     * @param date data początkowa
     * @param days ilość dni będzie dodana do daty początkowej
     * @param dayType rodzaj dnia do dodania B - roboczy, C - kalendarzowy
     * @return nty dzień zadanego typu (data)
     */
    Date getNthDay(Date date, Integer days, DayType dayType);
}
