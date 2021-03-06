package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.dto.security.GryfBlockableUserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;

import java.util.List;

/**
 * Serwis realizujacy uslugi dotyczace autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
public interface UserService {

    /**
     * Wyszukanie rol dla uzytkownika FO.
     *
     * @param login - login uzytkownika
     * @param password - haslo uzytkownika
     *
     * @return lista rol
     */
    List<String> findPrivilegesForFoLogin(String login, String password);

    /**
     * Wyszukanie uprawnień dla uzytkownika TI.
     *
     * @param login - login uzytkownika
     * @param password - hasło napisane tekstem (niezahaszowane)
     * @return lista uprawnień
     */
    List<String> findPrivilegesForTiLogin(String login, String password);

    /**
     * Metoda wyszukująca przywileje dla osoby fizycznej
     * @param pesel
     * @param verificationCode - kod weryfikacyjny (niezahaszowane)
     * @return lista przywilejów
     */
    List<String> findPrivilegesForIndPesel(String pesel, String verificationCode);

    /**
     * Ustawia datę ostatniego logowania dla uzytkownika TI
     *
     * @param gryfTiUser - zalogowany uzytkownik
     */
    void updateTiAfterSuccessLogin (GryfTiUser gryfTiUser);

    /**
     * Metoda wyszukująca id Usługodawcy dla użytkownika Usługodawcy
     * @param tiUserLogin
     * @return login Usługodawcy
     */
    Long findTrainingInstitutionIdForTiUser(String tiUserLogin);

    /**
     * Metoda odblokowująca użytkownika po spełnieniu odpowiednich warunków
     * @param user - dto użytkownika do odblokowania
     * @return - dto zaktualizowanego użytkownika
     */
    GryfBlockableUserDto unlockUser(GryfBlockableUserDto user);
}
