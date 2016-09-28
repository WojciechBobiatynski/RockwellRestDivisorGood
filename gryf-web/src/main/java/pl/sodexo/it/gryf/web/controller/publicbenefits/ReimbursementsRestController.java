package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementDeliveryService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementsAttachmentService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/reimbursements", produces = "application/json;charset=UTF-8")
public class ReimbursementsRestController {

    //FIELDS

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ReimbursementDeliveryService reimbursementDeliveryService;

    @Autowired
    private ReimbursementsAttachmentService reimbursementsAttachmentService;

    @Autowired
    private ReimbursementPatternService reimbursementPatternService;

    //PUBLIC METHODS - DELIVERIES

    @RequestMapping(value = "/deliveries", method = RequestMethod.GET)
    public List<ReimbursementDeliverySearchResultDTO> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES);
        return reimbursementDeliveryService.findReimbursementDeliveries(searchDTO);
    }

    @RequestMapping(value = "/reimbursableDeliveries", method = RequestMethod.GET)
    public List<ReimbursementDeliverySearchResultDTO> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES);
        return reimbursementDeliveryService.findReimbursableDeliveries(searchDTO);
    }
    
    
    @RequestMapping(value = "/delivery/{id}", method = RequestMethod.GET)
    public ReimbursementDeliveryDTO findReimbursementDelivery(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES);
        return reimbursementDeliveryService.findReimbursementDelivery(id);
    }

    @RequestMapping(value = "/delivery/save", method = RequestMethod.POST)
    public Long saveReimbursementDelivery(@RequestBody ReimbursementDeliveryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES_MOD);
        try {
            return reimbursementDeliveryService.saveReimbursementDelivery(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementDeliveryService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/delivery/settle", method = RequestMethod.POST)
    public Long settleReimbursementDelivery(@RequestBody ReimbursementDeliveryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES_REIMB);
        try {
            return reimbursementDeliveryService.settleReimbursementDelivery(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementDeliveryService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/delivery/cancel", method = RequestMethod.POST)
    public Long cancelReimbursementDelivery(@RequestBody ReimbursementDeliveryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES_CANCEL);
        try {
            return reimbursementDeliveryService.cancelReimbursementDelivery(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementDeliveryService.manageLocking(dto.getId());
        }
        return null;
    }

    //PUBLIC METHODS - REIMBURSEMENT

    @RequestMapping(value = "/reimbursements", method = RequestMethod.GET)
    public List<ReimbursementSearchResultDTO> findReimbursements(ReimbursementSearchQueryDTO searchDTO) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB);
        return reimbursementsAttachmentService.findReimbursementsSearchResults(searchDTO);
    }

    //PUBLIC MEHODS - DICTIONARIES

    @RequestMapping(value = "/reimbursementPatternsDictionaries", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findReimbursementPatternsDictionaries() {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_DELIVERIES, Privileges.GRF_PBE_REIMB);
        return reimbursementDeliveryService.findReimbursementPatternsDictionaries();
    }

}
