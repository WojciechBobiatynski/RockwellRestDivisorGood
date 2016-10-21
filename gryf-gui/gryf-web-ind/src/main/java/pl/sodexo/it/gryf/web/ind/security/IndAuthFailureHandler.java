package pl.sodexo.it.gryf.web.ind.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.service.api.security.SecurityService;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Obsługa zdarzenia niepoprawnego zalogowania użytkownika
 *
 * Created by akmiecinski on 21.10.2016.
 */
public class IndAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private IndividualUserService individualUserService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    @Autowired
    private ApplicationParameters applicationParameters;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl(GryfConstants.IND_DEFAULT_FAILURE_LOGIN_URL);
        super.onAuthenticationFailure(request,response,exception);
        blockUserIfExceedLoginAttemptsCounter(request.getParameter(usernamePasswordAuthenticationFilter.getUsernameParameter()));
    }

    private void blockUserIfExceedLoginAttemptsCounter(String pesel){
        GryfIndUserDto indUserDto = securityService.findIndUserByPesel(pesel);
        if(indUserDto == null || !indUserDto.isActive()){
            return;
        }
        indUserDto.setLoginFailureAttempts(indUserDto.getLoginFailureAttempts() + 1);
        indUserDto.setLastLoginFailureDate(new Date());
        if(indUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxIndLoginFailureAttempts()){
            indUserDto.setActive(false);
        }
        individualUserService.saveIndUser(indUserDto);
    }

}
