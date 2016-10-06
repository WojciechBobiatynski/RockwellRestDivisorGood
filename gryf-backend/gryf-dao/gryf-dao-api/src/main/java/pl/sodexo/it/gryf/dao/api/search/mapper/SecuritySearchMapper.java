package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper do wyszukiwania ról i uprawnieñ uzytkowników
 * 
 * Created by jbentyn on 2016-10-06.
 */
public interface SecuritySearchMapper {

    List<String>  findTrinUserPrivileges(@Param("login") String login);
}
