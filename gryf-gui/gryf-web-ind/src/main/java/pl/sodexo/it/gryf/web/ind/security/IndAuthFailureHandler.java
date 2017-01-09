package pl.sodexo.it.gryf.web.ind.security;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
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
        //TODO pobierać póxniej przy pomocy spring data jpa dla zachowania spójności - nie będzie potrzeby decryptowania hasła
        GryfIndUserDto indUserDto = securityService.findIndUserByPesel(pesel);
        if (indUserDto == null || !indUserDto.isActive()) {
            return;
        }

        indUserDto.setRoles(Sets.newHashSet(securityService.findRolesForIndividualUser(indUserDto.getInuId())));
        indUserDto.setVerificationCode(AEScryptographer.decrypt(indUserDto.getVerificationCode()));
        indUserDto.setLoginFailureAttempts(indUserDto.getLoginFailureAttempts() + 1);
        indUserDto.setLastLoginFailureDate(new Date());

        if (indUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxLoginFailureAttempts()) {
            indUserDto.setActive(false);
        }

        individualUserService.saveIndUser(indUserDto);
    }

}
