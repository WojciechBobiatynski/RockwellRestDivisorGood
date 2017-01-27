package pl.sodexo.it.gryf.web.ti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static pl.sodexo.it.gryf.web.ti.util.TiPageConstant.*;

/**
 * Kontroler widokow glownych dla TI.
 * 
 * Created by akuchna on 2016-10-07.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class TiMainViewController {

    public static final String PAGE_MAIN = "/WEB-INF/page/index.jsp";

    @RequestMapping(value = PATH_TI_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_TI_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String dashboard(Model model) {
        return PAGE_MAIN;
    }

    @RequestMapping(value = PATH_HELP, method = RequestMethod.GET)
    public String help() {
        return PAGE_HELP;
    }

    @RequestMapping(value = "/prolongSession", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void prolongSession() { }
}