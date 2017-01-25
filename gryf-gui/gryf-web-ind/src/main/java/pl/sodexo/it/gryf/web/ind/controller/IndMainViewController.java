package pl.sodexo.it.gryf.web.ind.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.service.api.security.VerificationService;

import static pl.sodexo.it.gryf.web.ind.util.IndPageConstant.*;

/**
 * Kontroler widokow glownych dla IND.
 * 
 * Created by akuchna on 2016-10-14.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class IndMainViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndMainViewController.class);

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = PATH_IND_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_IND_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String welcome(Model model) {
        return MAIN_PAGE;
    }

    @RequestMapping(value = PATH_HELP, method = RequestMethod.GET)
    public String help() {
        return PAGE_HELP;
    }
}
