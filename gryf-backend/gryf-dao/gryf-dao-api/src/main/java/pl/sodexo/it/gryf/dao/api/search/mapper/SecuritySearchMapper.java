package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper do wyszukiwania ról i uprawnień uytkownikow
 * 
 * Created by jbentyn on 2016-10-06.
 */
public interface SecuritySearchMapper {

    List<String> findTIUserPrivileges(@Param("login") String login);
}
