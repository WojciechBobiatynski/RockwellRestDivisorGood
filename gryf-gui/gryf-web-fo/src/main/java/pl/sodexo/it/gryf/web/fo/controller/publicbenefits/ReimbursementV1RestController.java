package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.common.parsers.ReimbursementParser;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.common.util.WebUtils;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.io.IOException;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/reimbursements/v1", produces = "application/json;charset=UTF-8")
public class ReimbursementV1RestController {

    //FIELDS

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ReimbursementService reimbursementService;

    @RequestMapping(value = "/reimbursement/{id}", method = RequestMethod.GET)
    public ReimbursementDTO findReimbursement(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB);
        return reimbursementService.findReimbursement(id);
    }

    @RequestMapping(value = "/reimbursement/initial/{reimbursementDeliveryId}", method = RequestMethod.GET)
    public ReimbursementDTO createInitialReimbursement(@PathVariable Long reimbursementDeliveryId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_MOD);
        return reimbursementService.createInitialReimbursement(reimbursementDeliveryId);
    }

    @RequestMapping(value = "/reimbursement/save", method = RequestMethod.POST)
    public Long saveReimbursement(@RequestParam("data") String data,
                                  @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_MOD);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementService.saveReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/correct", method = RequestMethod.POST)
    public Long correctReimbursement(@RequestParam("data") String data,
                                     @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_CORR);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementService.correctReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/verify", method = RequestMethod.POST)
    public Long verifyReimbursement(@RequestParam("data") String data,
                                    @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_PROC);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementService.verifyReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/settle", method = RequestMethod.POST)
    public Long settleReimbursement(@RequestParam("data") String data,
                                    @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_PROC);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementService.settleReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/complete", method = RequestMethod.POST)
    public Long completeReimbursement(@RequestParam("data") String data,
                                      @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_FINISH);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementService.completeReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/cancel", method = RequestMethod.POST)
    public Long cancelReimbursement(@RequestBody ReimbursementDTO dto) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_CANCEL);

        try {
            return reimbursementService.cancelReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/generateconfirmation", method = RequestMethod.POST)
    public Long generateConfirmationReimbursement(@RequestBody ReimbursementDTO dto) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_FINISH);

        try {
            return reimbursementService.generateConfirmationReimbursement(dto);
        } catch (GryfOptimisticLockRuntimeException e) {
            reimbursementService.manageLocking(dto.getId());
        }
        return null;
    }

}

