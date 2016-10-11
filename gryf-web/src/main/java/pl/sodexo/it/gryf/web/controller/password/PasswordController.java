package pl.sodexo.it.gryf.web.controller.password;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by adziobek on 07.10.2016.
 *
 * Służy do obsługi zmiany hasła przez użytkonika.
 */
@Controller
@RequestMapping("/")
public class PasswordController {

    @RequestMapping(value = "/changePasswordFormData", method = RequestMethod.POST)
    @ResponseBody
    public PasswordChangeStatus changeUserPassword(@RequestBody PasswordData passwordData, HttpServletResponse response) {
        if (PasswordValidator.isValidNewPassword(passwordData)) {
            return new PasswordChangeStatus("OK", "Hasło zmienione pomyślnie");
        } else {
            return new PasswordChangeStatus("ERROR", "Nie udało się zmienić hasła. Sprubój ponownie!");
        }
    }
}