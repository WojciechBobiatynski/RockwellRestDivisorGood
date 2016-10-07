package pl.sodexo.it.gryf.web.controller.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import static pl.sodexo.it.gryf.web.utils.UrlConstants.*;


@Controller
@RequestMapping("/dictionaries")
public class DictionariesViewController {

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping("/zipCodes/")
    public String formZipCode(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_ZIP_CODES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "dictionaries/zipCodesIndex.jsp");
        return DEFAULT_VIEW;
    }

}
