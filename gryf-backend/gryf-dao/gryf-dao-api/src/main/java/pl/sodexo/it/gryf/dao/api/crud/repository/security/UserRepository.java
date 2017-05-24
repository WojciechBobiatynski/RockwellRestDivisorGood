package pl.sodexo.it.gryf.dao.api.crud.repository.security;

import java.util.List;

/**
 * Dao do operacji autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
public interface UserRepository {

    /**
     * Wyszukanie rol dla uzytkownika.
     * 
     * @param login - login uzytkownika
     * @param password - haslo uzytkownika
     * 
     * @return lista rol
     */
    List<String> findRolesForLogin(String login, String password);

    /**
     * Sprawdza czy stare hasło poprawne
     * @param login - login
     * @param oldPassword - stare hasło
     */
    void checkIfOldPasswordCorrect(String login, String oldPassword);

}
