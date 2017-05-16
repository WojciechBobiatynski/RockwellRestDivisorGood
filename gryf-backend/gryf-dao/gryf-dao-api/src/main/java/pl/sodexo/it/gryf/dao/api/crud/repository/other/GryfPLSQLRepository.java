package pl.sodexo.it.gryf.dao.api.crud.repository.other;

import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;
import pl.sodexo.it.gryf.model.api.FinanceNoteResult;
import pl.sodexo.it.gryf.model.enums.DayType;

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

    /**
     * Generuje numer konta na podstawie kodu
     * @param code - kod
     * @return numer konta
     */
    String generateAccountByCode(String code);

    /**
     * Generuje notę obciążeniowo-księgowa dla zamówienia.
     * @param orderId identyfikator zamówienia
     * @param userLogin
     * @return resultat wywolania
     */
    FinanceNoteResult createDebitNoteForOrder(Long orderId, String userLogin);

    /**
     * Generuje notę uznaniową dla rozliczenia.
     * @param reimbursmentId identyfikator zamówienia
     * @return resultat wywolania
     */
    FinanceNoteResult createCreditNoteForReimbursment(Long reimbursmentId);

    void generateInstancesPrintNumber(String productId, Long numberFrom, Long numberTo);

    /**
     * Metoda robi flush do bazy danych
     */
    void flush();

    /**
     * Metoda zmieniająca hasło dla użytkownika
     * @param username - login użytkownika
     * @param changePasswordDto - obiekt z danymi dotyczącymi haseł
     */
    void changePassword(String username, ChangePasswordDto changePasswordDto);
}
