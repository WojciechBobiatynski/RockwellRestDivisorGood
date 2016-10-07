package pl.sodexo.it.gryf.web.security.traininginstitutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTIUser;
import pl.sodexo.it.gryf.service.api.security.traininginstitutions.TrainingInstitutionUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jbentyn on 2016-10-04.
 */
public class TrainingInstitutionUserAuthenticationProvider implements AuthenticationProvider {

    //TODO uzyc lomboka
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingInstitutionUserAuthenticationProvider.class);

    @Autowired
    private TrainingInstitutionUserService trainingInstitutionUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken incomingAuthToken = (UsernamePasswordAuthenticationToken) authentication;
        String login = incomingAuthToken.getPrincipal().toString().trim();
        Object credentials = incomingAuthToken.getCredentials();
        String password = credentials.toString();

        LOGGER.debug("[AUTH] Logowanie uzytkownika Instytucji Szkoleniowej, login={}", login);

        //TODO autentykacja
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(login);

        GryfTIUser tiUser = new GryfTIUser(new UserDto(login), grantedAuthorities);
        UsernamePasswordAuthenticationToken successAuthToken = new UsernamePasswordAuthenticationToken(tiUser, credentials, grantedAuthorities);
        LOGGER.info("[AUTH] Wlogowany uzytkownik, tiUser={}, grantedAuthorities={}", tiUser, grantedAuthorities);

        return successAuthToken;

    }

    private List<GrantedAuthority> getGrantedAuthorities(String login){
        return trainingInstitutionUserService.findPrivilegesForLogin(login).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
