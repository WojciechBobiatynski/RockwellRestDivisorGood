package pl.sodexo.it.gryf.dao.api.search.dao.security;

import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;

import java.util.List;

/**
 * Dao do wyszukiwania uprawnień i ról użytkowników
 *
 * Created by akmiecinski on 18.10.2016.
 */
public interface SecuritySearchDao {

    List<String> findTiUserPrivileges(String login);

    /**
     * Metoda wyszukująca przywileje dla osoby fizycznej
     * @param pesel
     * @return lista przywilejów
     */
    List<String> findIndUserPrivileges(String pesel);

    /**
     * Metoda wyszukująca użytkownika osoby fizycznej po peselu
     * @param pesel - pesel osoby fizycznej
     * @return Dto reprezentujące użytkonwika osoby fizycznej na potrzeby autentykacji
     */
    GryfIndUserDto findIndUserByPesel(String pesel);

    /**
     * Metoda wyszukująca role dla Usługodawcy
     * @return lista ról
     */
    List<RoleDto> findRolesForTiUser();

    /**
     * Metoda wyszukująca role dla użytkownika osoby fizycznej
     * @return lista ról
     */
    List<RoleDto> findRolesForIndUser();

    /**
     * Metoda wyszukująca role dla zalogowanego użytkownika osoby fizycznej
     * @return lista ról
     */
    List<RoleDto> findRolesForIndividualUser(Long individualUserId);
    /**
     * Metoda wyszukująca id Usługodawcy dla użytkownika Usługodawcy
     * @param tiUserLogin
     * @return login Usługodawcy
     */
    Long findTrainingInstitutionIdForTiUser(String tiUserLogin);

}
