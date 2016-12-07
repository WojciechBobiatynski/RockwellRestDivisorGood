package pl.sodexo.it.gryf.web.fo.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

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

    public static final String PATH_GENERATE_PRINT_NUMBERS_PATCH = "/generatePrintNumbers";
    public static final String PATH_GENERATE_PRINT_NUMBERS_INDEX = "administration/generatePrintNumbers/printNumbersIndex.jsp";

    public static final String IMPORT_FROM_FILE_PATCH = "/asynchjobs";
    public static final String IMPORT_FROM_FILE_INDEX = "/administration/asynchjobs/asynchJobsIndex.jsp";

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = PATH_GENERATE_PRINT_NUMBERS_PATCH, method = RequestMethod.GET)
    public String generatePrintNumbersEnter(Model model) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + PATH_GENERATE_PRINT_NUMBERS_INDEX);
        return DEFAULT_VIEW;
    }

    @RequestMapping(value = IMPORT_FROM_FILE_PATCH, method = RequestMethod.GET)
    public String getImportFromFileView(Model model) {
        //TODO: security checker
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + IMPORT_FROM_FILE_INDEX);
        return DEFAULT_VIEW;
    }

}
