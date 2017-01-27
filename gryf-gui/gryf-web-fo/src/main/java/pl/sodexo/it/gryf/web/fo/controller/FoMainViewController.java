package pl.sodexo.it.gryf.web.fo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static pl.sodexo.it.gryf.web.common.util.PageUtil.setUpMainContent;
import static pl.sodexo.it.gryf.web.fo.utils.FoPageConstant.*;

/**
 * Kontroler widokow glownych dla FO.
 * 
 * Created by akuchna on 2016-10-13.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class FoMainViewController {

    @RequestMapping(value = PATH_FO_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_FO_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String welcome(Model model) {
        setUpMainContent(model, SUB_PAGE_FO_WELCOME);
        //return PAGE_MAIN;
        return "/WEB-INF/pages/index.jsp"; //todo ak poprawic
    }

    @RequestMapping(value = "/prolongSession", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void prolongSession() { }

}
