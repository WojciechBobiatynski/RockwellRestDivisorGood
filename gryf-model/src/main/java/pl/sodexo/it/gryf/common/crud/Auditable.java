package pl.sodexo.it.gryf.common.crud;

import java.util.Date;

/**
 * Interfejs oznaczajacy klase z danymi audytowymi.
 *
 * Created by akmiecinski on 2016-10-03.
 */
public interface Auditable {

    /**
     * Zwraca użytkownika aplikacji, który stworzył dany obiekt
     * @return
     */
    String getCreatedUser();

    /**
     * Zwraca użytkownika aplikacji, który zmodyfikował dany obiekt
     * @return
     */
    String getModifiedUser();

    /**
     * Zwraca timestamp utworzenia obiektu
     * @return
     */
    Date getCreatedTimestamp();

    /**
     * Zwraca timestamp zmodyfikowania obiektu
     * @return
     */
    Date getModifiedTimestamp();

}
