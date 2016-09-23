package pl.sodexo.it.gryf.dao.api.crud.repository;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Created by akmiecinski on 2016-09-22.
 */
public interface SecurityRepository {

    List<String> findRolesForLogin(String login);

}
