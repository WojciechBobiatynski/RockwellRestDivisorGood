package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.UnrsvPoolRmbsDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.PATH_UNRESERVED_POOL_REIMBURSEMENTS;
import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.PATH_UNRESERVED_POOL_REIMBURSEMENTS_FIND;

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

}
