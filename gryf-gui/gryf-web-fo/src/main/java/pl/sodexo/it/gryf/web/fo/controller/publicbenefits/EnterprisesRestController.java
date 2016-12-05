package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/enterprise",
        produces = "application/json;charset=UTF-8")
public class EnterprisesRestController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public EnterpriseDto getNewEnterprise() {
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.createEnterprise();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveEnterprise(@RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                               @RequestBody EnterpriseDto enterpriseDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        return enterpriseService.saveEnterpriseDto(enterpriseDto, checkVatRegNumDup, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EnterpriseDto getEnterprise(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprise(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateEnterprise(@PathVariable Long id,
                                 @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                 @RequestBody EnterpriseDto enterpriseDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, enterpriseDto.getId());

        enterpriseService.updateEnterpriseDto(enterpriseDto, checkVatRegNumDup, true);
        return enterpriseDto.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO dto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISES);
        return enterpriseService.findEnterprises(dto);

    }
}
