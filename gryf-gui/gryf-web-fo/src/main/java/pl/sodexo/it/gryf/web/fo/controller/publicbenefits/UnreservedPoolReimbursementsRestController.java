package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.UnrsvPoolRmbsDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsMailService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.UnreservedPoolReimbursementService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

/**
 * Kontroler dla elektornicznych rozliczeń niewykorzystanej puli bonów
 *
 * Created by akmiecinski on 22.12.2016.
 */
//TODO: Ze względu na nowe typy rozliczeń, należy zmienić nazwę, ponieważ rozliczenie zwróconej puli bonów jak i niewykorzytsnaje puli korzysta z tego samego kontrolera
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + PATH_UNRESERVED_POOL_REIMBURSEMENTS, produces = "application/json;charset=UTF-8")
public class UnreservedPoolReimbursementsRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnreservedPoolReimbursementsRestController.class);

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private UnreservedPoolReimbursementService unreservedPoolReimbursementService;

    @Autowired
    private ErmbsMailService ermbsMailService;

    @RequestMapping(value = PATH_UNRESERVED_POOL_REIMBURSEMENTS_FIND + "{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public UnrsvPoolRmbsDto findElctRmbsById(@PathVariable Long ermbsId){
        LOGGER.debug("findElctRmbsById, ermbsId={}", ermbsId);
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return unreservedPoolReimbursementService.findUnrsvPoolRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_CREATE_DOCUMENTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto createDocuments(@PathVariable("rmbsId") Long rmbsId) {
        LOGGER.debug("createDocuments, rmbsId={}", rmbsId);
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.createDocuments(rmbsId);
        return unreservedPoolReimbursementService.findUnrsvPoolRmbsById(id);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_PRINT_REPORTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto printReports(@PathVariable("rmbsId") Long rmbsId) {
        LOGGER.debug("printReports, rmbsId={}", rmbsId);
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.printReports(rmbsId);
        return unreservedPoolReimbursementService.findUnrsvPoolRmbsById(id);
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_CREATE_EMAIL_FROM_TEMPLATE + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public List<ErmbsMailDto> createEmailsFromTemplate(@PathVariable("rmbsId") Long rmbsId) {
        LOGGER.debug("createEmailsFromTemplate, rmbsId={}", rmbsId);
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        List<ErmbsMailDto> mailFromTemplates = ermbsMailService.createMailFromTemplatesForUnreservedPool(rmbsId);
        return mailFromTemplates;
    }

    @RequestMapping(value = PATH_UNRESERVED_POOL_EXPIRE + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public UnrsvPoolRmbsDto expire(@PathVariable("rmbsId") Long rmbsId) {
        LOGGER.debug("expire, rmbsId={}", rmbsId);
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.expire(rmbsId);
        return unreservedPoolReimbursementService.findUnrsvPoolRmbsById(id);
    }

}
