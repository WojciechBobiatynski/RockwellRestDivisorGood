package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.common.parsers.ReimbursementParser;
import pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement.ReimbursementV1Service;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;
import pl.sodexo.it.gryf.web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/reimbursements/v1", produces = "application/json;charset=UTF-8")
public class ReimbursementV1RestController {

    //FIELDS

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ReimbursementV1Service reimbursementV1Service;

    @RequestMapping(value = "/reimbursement/{id}", method = RequestMethod.GET)
    public ReimbursementDTO findReimbursement(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB);
        return reimbursementV1Service.findReimbursement(id);
    }

    @RequestMapping(value = "/reimbursement/initial/{reimbursementDeliveryId}", method = RequestMethod.GET)
    public ReimbursementDTO createInitialReimbursement(@PathVariable Long reimbursementDeliveryId) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_MOD);
        return reimbursementV1Service.createInitialReimbursement(reimbursementDeliveryId);
    }

    @RequestMapping(value = "/reimbursement/save", method = RequestMethod.POST)
    public Long saveReimbursement(@RequestParam("data") String data,
                                  @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_MOD);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementV1Service.saveReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/correct", method = RequestMethod.POST)
    public Long correctReimbursement(@RequestParam("data") String data,
                                     @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_CORR);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementV1Service.correctReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/verify", method = RequestMethod.POST)
    public Long verifyReimbursement(@RequestParam("data") String data,
                                    @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_PROC);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementV1Service.verifyReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/settle", method = RequestMethod.POST)
    public Long settleReimbursement(@RequestParam("data") String data,
                                    @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_PROC);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementV1Service.settleReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/complete", method = RequestMethod.POST)
    public Long completeReimbursement(@RequestParam("data") String data,
                                      @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_FINISH);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        ReimbursementDTO dto = ReimbursementParser.readReimbursement(data, fileDtoList);

        try {
            return reimbursementV1Service.completeReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/cancel", method = RequestMethod.POST)
    public Long cancelReimbursement(@RequestBody ReimbursementDTO dto) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_CANCEL);

        try {
            return reimbursementV1Service.cancelReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

    @RequestMapping(value = "/reimbursement/generateconfirmation", method = RequestMethod.POST)
    public Long generateConfirmationReimbursement(@RequestBody ReimbursementDTO dto) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_REIMB_FINISH);

        try {
            return reimbursementV1Service.generateConfirmationReimbursement(dto);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            reimbursementV1Service.manageLocking(dto.getId());
        }
        return null;
    }

}

