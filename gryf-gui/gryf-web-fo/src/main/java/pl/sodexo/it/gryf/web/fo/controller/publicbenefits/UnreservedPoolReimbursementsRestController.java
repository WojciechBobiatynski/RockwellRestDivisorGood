package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.UnrsvPoolRmbsDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

/**
 * Kontroler dla elektornicznych rozliczeń niewykorzystanej puli bonów
 *
 * Created by akmiecinski on 22.12.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + PATH_UNRESERVED_POOL_REIMBURSEMENTS, produces = "application/json;charset=UTF-8")
public class UnreservedPoolReimbursementsRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @RequestMapping(value = PATH_UNRESERVED_POOL_REIMBURSEMENTS_FIND + "{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public UnrsvPoolRmbsDto findElctRmbsById(@PathVariable Long ermbsId){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findUnrsvPoolRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_CREATE_DOCUMENTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto createDocuments(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.createDocuments(rmbsId);
        return electronicReimbursementsService.findUnrsvPoolRmbsById(id);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_PRINT_REPORTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto printReports(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.printReports(rmbsId);
        return electronicReimbursementsService.findUnrsvPoolRmbsById(id);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_EXPIRE + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto expire(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.expire(rmbsId);
        return electronicReimbursementsService.findUnrsvPoolRmbsById(id);
    }

}
