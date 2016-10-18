package pl.sodexo.it.gryf.web.fo.controller.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import static pl.sodexo.it.gryf.web.common.util.PageUtil.setUpMainContent;
import static pl.sodexo.it.gryf.web.fo.util.FoPageConstant.PATH_FO_ZIP;
import static pl.sodexo.it.gryf.web.fo.util.FoPageConstant.SUB_PAGE_FO_ZIP;

@Controller
@RequestMapping("/dictionaries")
public class DictionariesViewController {

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(PATH_FO_ZIP)
    public String formZipCode(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_ZIP_CODES);
        setUpMainContent(model, SUB_PAGE_FO_ZIP);
        return "/WEB-INF/page/index.jsp"; //todo ak poprawic
    }

}
