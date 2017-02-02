package pl.sodexo.it.gryf.web.ti.controller.password.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.common.exception.verification.GryfInvalidTokenException;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TiUserResetAttemptService;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;

import javax.servlet.http.HttpServletRequest;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.*;
import static pl.sodexo.it.gryf.web.ti.util.TiPageConstant.*;

/**
 * Kontroler związany za zapisem nowego hasła dla użytkownika Usługodawcy
 *
 * Created by akmiecinski on 26.10.2016.
 */
@Controller
public class SavePasswordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavePasswordController.class);

    private static final String TOKEN_PLACEHOLDER = "token";

    @Autowired
    private TiUserResetAttemptService tiUserResetAttemptService;

    @Autowired
    private TrainingInstitutionUserService trainingInstitutionUserService;

    @RequestMapping(value = PATH_RESET_PASSWORD + "/{turId}", method = RequestMethod.GET)
    public String resetPassword(@PathVariable(value = "turId") String turId, Model uiModel, HttpServletRequest request) {
        try {
            tiUserResetAttemptService.checkIfResetAttemptStillActive(turId);
            request.getSession().setAttribute(TOKEN_PLACEHOLDER, turId);
        } catch (GryfRuntimeException e) {
            LOGGER.error("Wystąpił błąd", e);
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, e);
            return PAGE_RESET_PASSWORD_EXCEPTION;
        } catch (Exception e) {
            LOGGER.error("Wystąpił niespodziewany błąd", e);
            uiModel.addAttribute(JSP_UNKNOWNERROR_PLACEHOLDER, e);
            return PAGE_RESET_PASSWORD_EXCEPTION;
        }
        return PAGE_RESET_PASSWORD;
    }

    @RequestMapping(value = PATH_SAVE_PASSWORD, method = RequestMethod.POST)
    public String savePassword(Model uiModel, HttpServletRequest request) {
        String password = request.getParameter(JSP_PASSWORD_PLACEHOLDER);
        String repeatedPassword = request.getParameter(JSP_REPEATED_PASSWORD_PLACEHOLDER);
        if (!password.equals(repeatedPassword)) {
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, new GryfVerificationException("Hasła muszą być identyczne"));
            return PAGE_RESET_PASSWORD;
        }
        if(!trainingInstitutionUserService.isPasswordStrong(password)){
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, new GryfVerificationException("Hasło powinno zawierać minimum 8 znaków. "
                    + "Hasło powinno zawierać przynajmniej jedną cyfrę, wielką literę oraz znak specjalny (np: # @ !)"));
            return PAGE_RESET_PASSWORD;
        }

        String token = (String) request.getSession().getAttribute(TOKEN_PLACEHOLDER);
        request.getSession().removeAttribute(TOKEN_PLACEHOLDER);

        if (token != null && token.equals(request.getParameter(TOKEN_PLACEHOLDER))) {
            try{
                GryfTiUserDto user = trainingInstitutionUserService.findUserByTurIdAndSaveNewPassword(token, password);
                uiModel.addAttribute(JSP_LOGIN_PLACEHOLDER, user.getLogin());
            } catch (GryfRuntimeException e){
                LOGGER.error("Blad podczas zapisywania hasła", e);
                uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, e);
            }
        } else {
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, new GryfInvalidTokenException("Niepoprawny token"));
            return PAGE_SAVE_SUCCESS;
        }
        return PAGE_SAVE_SUCCESS;
    }

}
