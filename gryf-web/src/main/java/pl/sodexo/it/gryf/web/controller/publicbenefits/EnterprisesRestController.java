package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.Privileges;
import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/enterprise",
        produces = "application/json;charset=UTF-8")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class EnterprisesRestController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Enterprise getNewEnterprise() {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.createEnterprise();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveEnterprise(@RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                               @RequestBody Enterprise enterprise) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        enterprise = enterpriseService.saveEnterprise(enterprise, checkVatRegNumDup);
        return enterprise.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Enterprise getEnterprise(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprise(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateEnterprise(@PathVariable Long id,
                                 @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                 @RequestBody Enterprise enterprise) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, enterprise.getId());

        enterpriseService.updateEnterprise(enterprise, checkVatRegNumDup);
        return enterprise.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprises(dto);

    }
}
