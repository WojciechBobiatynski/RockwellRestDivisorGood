package pl.sodexo.it.gryf.web.ti.controller.password.retrieve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.service.api.security.VerificationService;

import javax.servlet.http.HttpServletRequest;

import static pl.sodexo.it.gryf.web.ti.util.TiPageConstant.*;

/**
 * Kontroler obsługjący zdarzenia związane z odzyskaniem hasła dla instytucji szkoleniowej
 *
 * Created by akmiecinski on 25.10.2016.
 */
@Controller
public class RetrievePasswordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrievePasswordController.class);

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = PATH_RETRIEVE_PASSWORD, method = RequestMethod.GET)
    public String retrievePassword() {
        return PAGE_RETRIEVE_PASSWORD;
    }

    @RequestMapping(value = PATH_RETRIEVE_RESET_PASSWORD, method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request, Model uiModel) {
        String email = request.getParameter("email");
        return resetAndGetComeBackUrl(uiModel, email, request);
    }

    private String resetAndGetComeBackUrl(Model uiModel, String email, HttpServletRequest request) {
        String comebackPage;
        try {
            verificationService.resetTiUserPassword(email, getURLWithContextPath(request));
            uiModel.addAttribute("email", email);
            comebackPage = PAGE_RETRIEVE_RESET_PASSWORD_SUCCESS;
        } catch (GryfRuntimeException e){
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute("error", e);
            comebackPage = PAGE_RETRIEVE_PASSWORD;
        } catch(Exception e) {
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute("unknowerror", e);
            comebackPage = PAGE_RETRIEVE_PASSWORD;
        }
        return comebackPage;
    }

    private static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
