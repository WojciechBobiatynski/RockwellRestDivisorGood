package pl.sodexo.it.gryf.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.dao.api.crud.repository.SecurityRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmiecinski on 2016-09-22.
 */
@Component
public class GryfAuthenticationProvider implements AuthenticationProvider{

    //TODO:moim zdaniem powinno być w dao
    @Autowired
    DataSource dataSource;

    @Autowired
    private SecurityRepository securityRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) authentication;
        String login = upat.getPrincipal().toString().toUpperCase();
        String password = (String)upat.getCredentials();

        try (Connection dsConnection = dataSource.getConnection(); Connection conn = DriverManager.getConnection(dsConnection.getMetaData().getURL(), login, password)){
            List<GrantedAuthority> authorities = getGrantedAuthorities(login);
            return new UsernamePasswordAuthenticationToken(login, upat.getCredentials(), authorities);

        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się nazwiazać połączenia przy autentykacji", e);
        } catch (Exception ex) {
            throw new BadCredentialsException("wrong credentials or a db problem (" + ex.getMessage() + ")");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(String login) {
        List<String> augids = securityRepository.findRolesForLogin(login);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String augid : augids) {
            authorities.add(new SimpleGrantedAuthority(augid));
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
