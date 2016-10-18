package pl.sodexo.it.gryf.web.fo.security;

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
import pl.sodexo.it.gryf.common.dto.user.GryfFoUser;
import pl.sodexo.it.gryf.common.exception.GryfUnknownException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfAuthenticationException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.service.api.security.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provider autentykacji dla FO.
 * 
 * Created by akuchna on 2016-10-13.
 */
public class FoUserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoUserAuthenticationProvider.class);

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
        GryfFoUser gryfFoUser = new GryfFoUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken succesAuthToken = new UsernamePasswordAuthenticationToken(gryfFoUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik, gryfFoUser={}, grantedAuthorities={}", gryfFoUser, grantedAuthorities);

        return succesAuthToken;
    }

    private List<GrantedAuthority> getGrantedAuthorities(String login, String password) {
        try {
            return userService.findPrivilegesForFoLogin(login, password).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
