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

    public static final String ASYNCH_JOBS_PATH = "/asynchjobs";
    public static final String ASYNCH_JOBS_INDEX = "/administration/asynchjobs/asynchJobsIndex.jsp";

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = ASYNCH_JOBS_PATH, method = RequestMethod.GET)
    public String getImportFromFileView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_ASYNCH_JOBS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + ASYNCH_JOBS_INDEX);
        return DEFAULT_VIEW;
    }

}
