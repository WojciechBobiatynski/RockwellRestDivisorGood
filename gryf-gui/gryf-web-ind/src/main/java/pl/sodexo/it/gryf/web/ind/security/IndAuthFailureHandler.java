package pl.sodexo.it.gryf.web.ind.security;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.service.api.security.SecurityService;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.web.common.security.AuthFailureHandler;

import java.util.Date;

/**
 * Obsługa zdarzenia niepoprawnego zalogowania użytkownika osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
public class IndAuthFailureHandler extends AuthFailureHandler {

    @Autowired
    private IndividualUserService individualUserService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ApplicationParameters applicationParameters;

    protected void blockUserIfExceedLoginAttemptsCounter(String pesel) {
        GryfIndUserDto indUserDto = securityService.findIndUserByPesel(pesel);
        if (indUserDto == null || !indUserDto.isActive()) {
            return;
        }

        indUserDto.setLoginFailureAttempts(indUserDto.getLoginFailureAttempts() + 1);
        indUserDto.setLastLoginFailureDate(new Date());

        if (indUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxLoginFailureAttempts()) {
            indUserDto.setActive(false);
        }

        individualUserService.saveIndUser(indUserDto);
    }

}
