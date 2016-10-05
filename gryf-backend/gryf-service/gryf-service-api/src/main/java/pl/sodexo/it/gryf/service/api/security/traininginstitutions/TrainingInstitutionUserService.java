package pl.sodexo.it.gryf.service.api.security.traininginstitutions;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-04.
 */
public interface TrainingInstitutionUserService {
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
