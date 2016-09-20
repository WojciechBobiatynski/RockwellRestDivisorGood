package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.Privileges;
import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.GrantApplicationParser;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;
import pl.sodexo.it.gryf.web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/grantapplication", produces = "application/json;charset=UTF-8")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class GrantApplicationsRestController {

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private GrantApplicationsService grantApplicationsService;

    //PUBLIC METHODS

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<GrantApplicationSearchResultDTO> findApplication(GrantApplicationSearchQueryDTO searchDTO) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationsService.findApplications(searchDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getApplicationFormDataById(@PathVariable Long id) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationsService.findApplicationFormData(id);
    }

    @RequestMapping(value = "/save/{versionId}", method = RequestMethod.POST)
    public Long saveApplication(@PathVariable Long versionId,
                                @RequestParam("data") String data,
                                @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_MOD);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        return grantApplicationsService.saveApplication(versionId, data, fileDtoList);
    }

    @RequestMapping(value = "/update/{versionId}/{id}", method = RequestMethod.POST)
    public Long updateApplication(@PathVariable Long versionId,
                                  @PathVariable Long id,
                                  @RequestParam(value = "data") String data,
                                  @RequestParam(value = "file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_MOD);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationsService.updateApplication(versionId, data, fileDtoList);

        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            grantApplicationsService.manageLocking(id);
        }
        return null;
    }


    @RequestMapping(value = "/apply/{versionId}", method = RequestMethod.POST)
    public Long applyApplication(@PathVariable Long versionId,
                                 @RequestParam("data") String data,
                                 @RequestParam(value = "acceptedViolationsParam", required = false) String grantApplicationApplyParams,
                                 @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_PROC);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        List<String> acceptedViolations = GrantApplicationParser.readAcceptedViolations(grantApplicationApplyParams);

        return grantApplicationsService.applyApplication(versionId, data, fileDtoList, acceptedViolations);
    }

    @RequestMapping(value = "/apply/{versionId}/{id}", method = RequestMethod.POST)
    public Long applyApplication(@PathVariable Long versionId,
                                 @PathVariable Long id,
                                 @RequestParam("data") String data,
                                 @RequestParam(value = "acceptedViolationsParam", required = false) String acceptedViolationsParam,
                                 @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_PROC);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            List<String> acceptedViolations = GrantApplicationParser.readAcceptedViolations(acceptedViolationsParam);
            return grantApplicationsService.applyApplication(versionId, data, fileDtoList, acceptedViolations);

        } catch (JpaOptimisticLockingFailureException | OptimisticLockException o) {
            grantApplicationsService.manageLocking(id);
        } catch (javax.persistence.OptimisticLockException e) {
            grantApplicationsService.manageLocking(id);
        }
        return null;
    }

    @RequestMapping(value = "/execute/{id}", method = RequestMethod.POST)
    public Long executeApplication(@PathVariable Long id,
                                   @RequestParam("data") String data,
                                   @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                   @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_PROC);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationsService.executeApplication(id, data, fileDtoList, checkVatRegNumDup);

        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            grantApplicationsService.manageLocking(id, "Wystąpił konflikt przy zapisywaniu wniosku lub MŚP");
        }
        return null;
    }

    @RequestMapping(value = "/reject/{id}", method = RequestMethod.POST)
    public Long rejectApplication(@PathVariable Long id,
                                  @RequestParam("data") String data,
                                  @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATION_PROC);

        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationsService.rejectApplication(id, data, fileDtoList);

        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            grantApplicationsService.manageLocking(id, "Wystąpił konflikt przy zapisywaniu wniosku lub MŚP");
        }
        return null;
    }

    //PUBLIC MEHODS - DICTIONARIES

    @RequestMapping(value = "/grantProgramsDictionaries", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findGrantProgramsDictionaries() {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationsService.FindGrantProgramsDictionaries();
    }

    @RequestMapping(value = "/grantApplicationVersionsDictionaries/{grantProgramId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(@PathVariable Long grantProgramId) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationsService.findGrantApplicationVersionsDictionaries(grantProgramId);
    }

}
