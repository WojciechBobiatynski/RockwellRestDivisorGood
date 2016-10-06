package pl.sodexo.it.gryf.service.api.security.traininginstitutions;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-04.
 */
public interface TrainingInstitutionUserService {
    /**
     * Wyszukanie uprawnień dla uzytkownika.
     *
     * @param login - login uzytkownika
     *
     * @return lista uprawnień
     */
    List<String> findPrivilegesForLogin(String login);
}
