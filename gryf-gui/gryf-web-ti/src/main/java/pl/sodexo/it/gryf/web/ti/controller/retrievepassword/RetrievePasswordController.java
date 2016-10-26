package pl.sodexo.it.gryf.web.ti.controller.retrievepassword;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static pl.sodexo.it.gryf.web.ti.util.TiPageConstant.*;

/**
 * Kontroler obsługjący zdarzenia związane z odzyskaniem hasła dla instytucji szkoleniowej
 *
 * Created by akmiecinski on 25.10.2016.
 */
@Controller
public class RetrievePasswordController {

    @RequestMapping(value = PATH_RETRIEVE_PASSWORD, method = RequestMethod.GET)
    public String retrievePassword() {
        return PAGE_RETRIEVE_PASSWORD;
    }

    @RequestMapping(value = PATH_RETRIEVE_RESET_PASSWORD, method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request, Model uiModel) {
        String email = request.getParameter("email");

        return PAGE_RETRIEVE_PASSWORD;
    }
}
