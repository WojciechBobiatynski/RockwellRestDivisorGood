package pl.sodexo.it.gryf.web.controller.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;

import static pl.sodexo.it.gryf.web.ViewResolverAttributes.DEFAULT_VIEW;
import static pl.sodexo.it.gryf.web.ViewResolverAttributes.MAIN_CONTENT_PARAM_NAME;
import static pl.sodexo.it.gryf.web.controller.ControllersUrls.PAGES_PREFIX;


@Controller
@RequestMapping("/dictionaries")
public class DictionariesViewController {

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @RequestMapping("/zipCodes/")
    public String formZipCode(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ZIP_CODES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "dictionaries/zipCodesIndex.jsp");
        return DEFAULT_VIEW;
    }

}
