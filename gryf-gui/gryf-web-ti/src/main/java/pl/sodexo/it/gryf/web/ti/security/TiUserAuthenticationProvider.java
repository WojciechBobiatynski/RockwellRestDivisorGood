package pl.sodexo.it.gryf.web.ti.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfPasswordExpiredException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfUserNotActiveException;
import pl.sodexo.it.gryf.service.api.security.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provider autentykacji dla TI.
 * 
 * Created by akuchna on 2016-10-07.
 */
public class TiUserAuthenticationProvider implements AuthenticationProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TiUserAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken incomingAuthToken = (UsernamePasswordAuthenticationToken) authentication;
        String login = incomingAuthToken.getPrincipal().toString().trim();
        Object credentials = incomingAuthToken.getCredentials();
        String password = credentials.toString();

        LOGGER.debug("[AUTH] Logowanie uzytkownika TI, login={}", login);
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(login, password);
        GryfTiUser tiUser = new GryfTiUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken successAuthToken = new UsernamePasswordAuthenticationToken(tiUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik TI, tiUser={}, grantedAuthorities={}", tiUser, grantedAuthorities);

        return successAuthToken;
    }

    private List<GrantedAuthority> getGrantedAuthorities(String login, String password) {
        try {
            return userService.findPrivilegesForTiLogin(login, password).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } catch (GryfBadCredentialsException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Bledne dane logowania uzytkownika, login={}", login);
            throw new BadCredentialsException(e.getMessage(), e);
        } catch (GryfUserNotActiveException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Uzytkownik nieaktywny, login={}", login);
            throw new DisabledException(e.getMessage(), e);
        } catch (GryfPasswordExpiredException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Haslo uzytownika wygaslo, login={}", login);
            throw new CredentialsExpiredException(e.getMessage() ,e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
