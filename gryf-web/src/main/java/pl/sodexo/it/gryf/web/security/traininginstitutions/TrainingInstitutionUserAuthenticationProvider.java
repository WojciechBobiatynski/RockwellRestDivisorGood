package pl.sodexo.it.gryf.web.security.traininginstitutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTrInUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jbentyn on 2016-10-04.
 */
public class TrainingInstitutionUserAuthenticationProvider implements AuthenticationProvider {

    //TODO uzyc lomboka
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingInstitutionUserAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken incomingAuthToken = (UsernamePasswordAuthenticationToken) authentication;
        String login = incomingAuthToken.getPrincipal().toString().toUpperCase();
        Object credentials = incomingAuthToken.getCredentials();
        String password = credentials.toString();

        LOGGER.debug("[AUTH] Logowanie uzytkownika Instytucji Szkoleniowej, login={}", login);

        //TODO mock
        List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority [] {new SimpleGrantedAuthority("TEST_AUTHORITY")});


        GryfTrInUser tiUser = new GryfTrInUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken succesAuthToken = new UsernamePasswordAuthenticationToken(tiUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik, tiUser={}, grantedAuthorities={}", tiUser, grantedAuthorities);

        return succesAuthToken;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
