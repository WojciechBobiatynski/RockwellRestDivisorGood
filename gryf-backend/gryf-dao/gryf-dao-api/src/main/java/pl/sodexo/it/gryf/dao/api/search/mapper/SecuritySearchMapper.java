package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.dto.security.GryfIndSecurityDto;

import java.util.List;

/**
 * Mapper do wyszukiwania ról i uprawnień uytkownikow
 * 
 * Created by jbentyn on 2016-10-06.
 */
public interface SecuritySearchMapper {

    List<String> findTiUserPrivileges(@Param("login") String login);

    /**
     * Metoda wyszukująca przywileje dla osoby fizycznej
     * @param pesel
     * @return lista przywilejów
     */
    List<String> findIndUserPrivileges(@Param("pesel") String pesel);

    /**
     * Metoda wyszukująca użytkownika osoby fizycznej po peselu
     * @param pesel - pesel osoby fizycznej
     * @return Dto reprezentujące użytkonwika osoby fizycznej na potrzeby autentykacji
     */
    GryfIndSecurityDto findIndUserByPesel(@Param("pesel") String pesel);
}
