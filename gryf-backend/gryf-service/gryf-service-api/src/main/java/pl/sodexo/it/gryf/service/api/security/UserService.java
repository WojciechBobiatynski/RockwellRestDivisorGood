package pl.sodexo.it.gryf.service.api.security;

import java.util.List;

/**
 * Serwis realizujacy uslugi dotyczace autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
public interface UserService {

    /**
     * Wyszukanie rol dla uzytkownika.
     *
     * @param login - login uzytkownika
     * @param password - haslo uzytkownika
     *
     * @return lista rol
     */
    List<String> findRolesForLogin(String login, String password);

}
