package pl.sodexo.it.gryf.web.fo.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.password.PasswordService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

/**
 * Kontroler dla operacji związaych z hasłem po stronie FO
 *
 * Created by akmiecinski on 16.05.2017.
 */
@RestController
@RequestMapping(value = "administration/pass/change", produces = "application/json;charset=UTF-8")
public class PasswordController {

    //TODO: stwórz logger

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public void confirmPasswordChange(@RequestBody ChangePasswordDto dto) {
        //TODO: zaloguj akcję zmiany hasła
        //TODO: zmienić uprawnienia
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES_MOD);
        passwordService.changePassword(dto);
    }

}
