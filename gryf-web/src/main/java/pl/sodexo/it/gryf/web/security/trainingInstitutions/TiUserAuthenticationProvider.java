package pl.sodexo.it.gryf.web.security.trainingInstitutions;

import lombok.extern.slf4j.Slf4j;
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
 * Authentication Provider dla użytkowników instytucji szkoleniowej
 *
 * Created by jbentyn on 2016-10-04.
 */
@Slf4j
public class TiUserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken incomingAuthToken = (UsernamePasswordAuthenticationToken) authentication;
        String login = incomingAuthToken.getPrincipal().toString().trim();
        Object credentials = incomingAuthToken.getCredentials();
        String password = credentials.toString();

        LOGGER.debug("[AUTH] Logowanie uzytkownika Instytucji Szkoleniowej, login={}", login);

        List<GrantedAuthority> grantedAuthorities = null;
        try {
            grantedAuthorities = getGrantedAuthorities(login, password);
        } catch (GryfBadCredentialsException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Bledne dane logowania uzytkownika, login={}",login);
            throw new BadCredentialsException(e.getMessage(),e);
        } catch (GryfUserNotActiveException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Uzytkownik nieaktywny, login={}",login);
            throw new DisabledException(e.getMessage(),e);
        } catch (GryfPasswordExpiredException e) {
            LOGGER.info("[AUTH] Blad autoryzacji. Haslo uzytownika wygaslo, login={}",login);
            throw new CredentialsExpiredException(e.getMessage(),e);
        }

        GryfTiUser tiUser = new GryfTiUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken successAuthToken = new UsernamePasswordAuthenticationToken(tiUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik, tiUser={}, grantedAuthorities={}", tiUser, grantedAuthorities);

        return successAuthToken;

    }

    private List<GrantedAuthority> getGrantedAuthorities(String login, String password) {
        return userService.findPrivilegesForTiLogin(login, password).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
