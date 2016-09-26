package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/enterprise",
        produces = "application/json;charset=UTF-8")
 //TODO uzycie encji
public class EnterprisesRestController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public EnterpriseDto getNewEnterprise() {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.createEnterprise();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveEnterprise(@RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                               @RequestBody EnterpriseDto enterpriseDto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        enterpriseDto = enterpriseService.saveEnterpriseDto(enterpriseDto, checkVatRegNumDup);
        return enterpriseDto.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EnterpriseDto getEnterprise(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprise(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateEnterprise(@PathVariable Long id,
                                 @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                 @RequestBody EnterpriseDto enterpriseDto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, enterpriseDto.getId());

        enterpriseService.updateEnterpriseDto(enterpriseDto, checkVatRegNumDup);
        return enterpriseDto.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprises(dto);

    }
}
