package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.dto.user.GryfUser;

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

    /**
     * Wyszukanie uprawnień dla uzytkownika TI.
     *
     * @param login - login uzytkownika
     * @param password - hasło napisane tekstem (niezahaszowane)
     * @return lista uprawnień
     */
    List<String> findPrivilegesForTiLogin(String login, String password);

    /**
     * Ustawia datę ostatniego logowania dla uzytkownika
     *
     * @param user - zalogowany uzytkownik
     */
    void updateLastLoginDate(GryfUser user);
}
