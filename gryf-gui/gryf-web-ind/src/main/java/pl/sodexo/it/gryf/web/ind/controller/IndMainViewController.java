package pl.sodexo.it.gryf.web.ind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static pl.sodexo.it.gryf.web.common.util.PageUtil.setUpMainContent;
import static pl.sodexo.it.gryf.web.ind.util.IndPageConstant.*;

/**
 * Kontroler widokow glownych dla IND.
 * 
 * Created by akuchna on 2016-10-14.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class IndMainViewController {

    @RequestMapping(value = PATH_IND_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_IND_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String welcome(Model model) {
        setUpMainContent(model, SUB_PAGE_IND_WELCOME);
        return PAGE_MAIN;
    }
}
