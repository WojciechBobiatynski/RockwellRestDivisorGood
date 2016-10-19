package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.security.GryfIndSecurityDto;

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
    GryfIndSecurityDto findIndUserByPesel(String pesel);

}
