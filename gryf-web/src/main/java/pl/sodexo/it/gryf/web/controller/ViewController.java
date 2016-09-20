package pl.sodexo.it.gryf.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;

import static pl.sodexo.it.gryf.web.ViewResolverAttributes.*;
import static pl.sodexo.it.gryf.web.controller.ControllersUrls.PAGES_PREFIX;

@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    SecurityCheckerService securityCheckerService;

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

    @RequestMapping(value = "/prolongSession", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void prolongSession() {
    }
}
