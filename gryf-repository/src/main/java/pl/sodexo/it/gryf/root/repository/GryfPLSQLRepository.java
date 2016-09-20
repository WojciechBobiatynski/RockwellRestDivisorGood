package pl.sodexo.it.gryf.root.repository;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.DayType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-09-04.
 */
@Repository
public class GryfPLSQLRepository {

    //FIELDS

    @PersistenceContext
    private EntityManager entityManager;

    //PUBLIC METHODS

    public Date getNextBusinessDay(Date date){
        String nativeSql = "select PK_GRF_UTILS.Next_Business_Day(?1) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        return (Date)query.getSingleResult();
    }

    /**
     * Funkcja zwraca nty dzień roboczy
     * @param date data początkowa
     * @param days ilość dni roboczych będzie dodana do daty początkowej
     * @return nty dzień roboczy (data)
     */
    public Date getNthBusinessDay(Date date, Integer days){
        String nativeSql = "select PK_GRF_UTILS.Get_Nth_Business_Day(?1, ?2) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        query.setParameter(2, days);
        return (Date)query.getSingleResult();
    }
    
    /**
     * Funkcja zwraca nty dzień zadanego typu, B - roboczy, C - kalendarzowy
     * @param date data początkowa
     * @param days ilość dni będzie dodana do daty początkowej
     * @param dayType rodzaj dnia do dodania B - roboczy, C - kalendarzowy
     * @return nty dzień zadanego typu (data)
     */
    public Date getNthDay(Date date, Integer days, DayType dayType){
        String nativeSql = "select PK_GRF_UTILS.Get_Nth_Day(?1, ?2, ?3) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        query.setParameter(2, days);
        query.setParameter(3, dayType.name());
        return (Date)query.getSingleResult();
    }
    
}
