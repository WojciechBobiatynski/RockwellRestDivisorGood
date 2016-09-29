package pl.sodexo.it.gryf.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfFinOpUser;
import pl.sodexo.it.gryf.common.exception.GryfUnknownException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfAuthenticationException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.service.api.security.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacja AuthenticationProvider spring security.
 * Akceptuje autentykacje za pomoca loginu i hasla.
 * 
 * Created by akmiecinski on 2016-09-22.
 */
public class GryfAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GryfAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken incomingAuthToken = (UsernamePasswordAuthenticationToken) authentication;
        String login = incomingAuthToken.getPrincipal().toString().toUpperCase();
        Object credentials = incomingAuthToken.getCredentials();
        String password = credentials.toString();
        
        LOGGER.debug("[AUTH] Logowanie uzytkownika, login={}", login);
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(login, password);
        GryfFinOpUser gryfFinOpUser = new GryfFinOpUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken succesAuthToken = new UsernamePasswordAuthenticationToken(gryfFinOpUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik, gryfFinOpUser={}, grantedAuthorities={}", gryfFinOpUser, grantedAuthorities);
        
        return succesAuthToken;
    }

    private List<GrantedAuthority> getGrantedAuthorities(String login, String password) {
        try {
            return userService.findRolesForLogin(login, password).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } catch (GryfBadCredentialsException e) {
            LOGGER.info("[AUTH] Nieprawidlowe logowanie, login={}", login);
            throw new BadCredentialsException("Nieprawidlowy uzytkownik lub haslo", e);
        } catch (GryfAuthenticationException e) {
            LOGGER.error("Blad podczas autentykacji", e);
            throw new GryfUnknownException("Blad podczas autentykacji", e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
