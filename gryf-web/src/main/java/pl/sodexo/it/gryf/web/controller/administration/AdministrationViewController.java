package pl.sodexo.it.gryf.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static pl.sodexo.it.gryf.web.utils.UrlConstants.*;

/**
 * Kontroler MVC dla zakładki administracja
 *
 * Created by jbentyn on 2016-10-10.
 */
@Controller
@RequestMapping(AdministrationViewController.PATH_ADMINISTRATION)
public class AdministrationViewController {

    //TODO  przenieść do stałych po rozdzieleniu weba
    public static final String PATH_ADMINISTRATION = "/administration";
    public static final String PATH_GENERATE_PRINT_NUMBERS = "/generatePrintNumbers";
    public static final String PATH_GENERATE_PRINT_NUMBERS_INDEX = "administration/generatePrintNumbers/printNumbersIndex.jsp";

    @RequestMapping(value = PATH_GENERATE_PRINT_NUMBERS, method = RequestMethod.GET)
    public String generatePrintNumbersEnter(Model model) {
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + PATH_GENERATE_PRINT_NUMBERS_INDEX);
        return DEFAULT_VIEW;
    }

}
