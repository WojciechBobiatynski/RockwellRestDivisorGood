package pl.sodexo.it.gryf.web.ti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static pl.sodexo.it.gryf.web.common.utils.PageUtils.setUpMainContent;
import static pl.sodexo.it.gryf.web.ti.utils.TiPageConstants.*;

/**
 * Kontroler widokow glownych dla TI.
 * 
 * Created by akuchna on 2016-10-07.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class TiMainViewController {

    @RequestMapping(value = PATH_TI_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_TI_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String welcome(Model model) {
        setUpMainContent(model, SUB_PAGE_TI_WELCOME);
        return PAGE_MAIN;
    }
    
}