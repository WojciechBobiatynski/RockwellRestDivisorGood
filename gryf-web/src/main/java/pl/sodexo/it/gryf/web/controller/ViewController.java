package pl.sodexo.it.gryf.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import static pl.sodexo.it.gryf.web.utils.UrlConstants.*;

@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    SecurityChecker securityChecker;

    // forms
    @RequestMapping("/")
    public String formIndex(Model model) {
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "gryf-web-index.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return LOGIN_VIEW;
    }

    @RequestMapping(value = "/ti/login", method = RequestMethod.GET)
    public String tiLogin() {
        return "/WEB-INF/pages/trainingInstitutionLogin.jsp";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword(Model model) {
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "changepassword/changePasswordIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping(value = "/prolongSession", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void prolongSession() {
    }
}
