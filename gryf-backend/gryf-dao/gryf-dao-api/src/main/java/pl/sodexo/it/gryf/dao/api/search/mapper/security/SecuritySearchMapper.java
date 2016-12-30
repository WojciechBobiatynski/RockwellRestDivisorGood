package pl.sodexo.it.gryf.dao.api.search.mapper.security;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;

import java.util.List;

/**
 * Mapper do wyszukiwania ról i uprawnień uytkownikow
 * 
 * Created by jbentyn on 2016-10-06.
 */
public interface SecuritySearchMapper {

    List<String> findTiUserPrivileges(@Param("criteria") UserCriteria criteria, @Param("login") String login);

    /**
     * Metoda wyszukująca przywileje dla osoby fizycznej
     * @param pesel
     * @return lista przywilejów
     */
    List<String> findIndUserPrivileges(@Param("criteria") UserCriteria criteria, @Param("pesel") String pesel);

    /**
     * Metoda wyszukująca użytkownika osoby fizycznej po peselu
     * @param pesel - pesel osoby fizycznej
     * @return Dto reprezentujące użytkonwika osoby fizycznej na potrzeby autentykacji
     */
    GryfIndUserDto findIndUserByPesel(@Param("criteria") UserCriteria criteria, @Param("pesel") String pesel);

    /**
     * Metoda wyszukująca role dla instytucji szkoleniowej
     * @param criteria - kryteria użytkownika
     * @return lista ról
     */
    List<RoleDto> findRolesForTiUser(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda wyszukująca role dla użytkownika osoby fizycznej
     * @param criteria - kryteria użytkownika
     * @return lista ról
     */
    List<RoleDto> findRolesForIndUser(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda wyszukująca role dla zalogowanego użytkownika osoby fizycznej
     * @param individualUserId - kryteria użytkownika
     * @return lista ról
     */
    List<RoleDto> findRolesForIndividualUser(@Param("individualUserId") Long individualUserId);
    /**
     * Metoda wyszukująca id instytucji szkoleniowej dla użytkownika instytucji szkoleniowej
     * @param tiUserLogin
     * @return login instytucji szkoleniowej
     */
    Long findTrainingInstitutionIdForTiUser(@Param("tiUserLogin") String tiUserLogin);
}
