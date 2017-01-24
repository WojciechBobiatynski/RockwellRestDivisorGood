package pl.sodexo.it.gryf.web.ti.security;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;
import pl.sodexo.it.gryf.web.common.security.AuthFailureHandler;

import java.util.Date;

/**
 * Obsługa zdarzenia niepoprawnego zalogowania użytkownika Usługodawcy
 *
 * Created by akmiecinski on 24.10.2016.
 */
public class TiAuthFailureHandler extends AuthFailureHandler {

    @Autowired
    private TrainingInstitutionUserService trainingInstitutionUserService;

    @Autowired
    private ApplicationParameters applicationParameters;

    protected void blockUserIfExceedLoginAttemptsCounter(String login) {
        GryfTiUserDto gryfTiUserDto = trainingInstitutionUserService.findTiUserByLogin(login);
        if (gryfTiUserDto == null || !gryfTiUserDto.isActive()) {
            return;
        }

        gryfTiUserDto.setLoginFailureAttempts(gryfTiUserDto.getLoginFailureAttempts() + 1);
        gryfTiUserDto.setLastLoginFailureDate(new Date());

        if (gryfTiUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxLoginFailureAttempts()) {
            gryfTiUserDto.setActive(false);
        }

        trainingInstitutionUserService.saveTiUser(gryfTiUserDto);
    }

}
