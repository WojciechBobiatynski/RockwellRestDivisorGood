package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.common.parsers.GrantApplicationParser;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationActionService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;
import pl.sodexo.it.gryf.web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/grantapplication", produces = "application/json;charset=UTF-8")
public class GrantApplicationsRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private GrantApplicationActionService grantApplicationActionService;

    //PUBLIC METHODS

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<GrantApplicationSearchResultDTO> findApplication(GrantApplicationSearchQueryDTO searchDTO) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationActionService.findApplications(searchDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getApplicationFormDataById(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationActionService.findApplicationFormData(id);
    }

    @RequestMapping(value = "/save/{versionId}", method = RequestMethod.POST)
    public Long saveApplication(@PathVariable Long versionId,
                                @RequestParam("data") String data,
                                @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_MOD);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        return grantApplicationActionService.saveApplication(versionId, data, fileDtoList);
    }

    @RequestMapping(value = "/update/{versionId}/{id}", method = RequestMethod.POST)
    public Long updateApplication(@PathVariable Long versionId,
                                  @PathVariable Long id,
                                  @RequestParam(value = "data") String data,
                                  @RequestParam(value = "file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_MOD);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationActionService.updateApplication(versionId, data, fileDtoList);

        } catch (GryfOptimisticLockRuntimeException e) {
            grantApplicationActionService.manageLocking(id);
        }
        return null;
    }


    @RequestMapping(value = "/apply/{versionId}", method = RequestMethod.POST)
    public Long applyApplication(@PathVariable Long versionId,
                                 @RequestParam("data") String data,
                                 @RequestParam(value = "acceptedViolationsParam", required = false) String grantApplicationApplyParams,
                                 @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_PROC);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        List<String> acceptedViolations = GrantApplicationParser.readAcceptedViolations(grantApplicationApplyParams);

        return grantApplicationActionService.applyApplication(versionId, data, fileDtoList, acceptedViolations);
    }

    @RequestMapping(value = "/apply/{versionId}/{id}", method = RequestMethod.POST)
    public Long applyApplication(@PathVariable Long versionId,
                                 @PathVariable Long id,
                                 @RequestParam("data") String data,
                                 @RequestParam(value = "acceptedViolationsParam", required = false) String acceptedViolationsParam,
                                 @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_PROC);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            List<String> acceptedViolations = GrantApplicationParser.readAcceptedViolations(acceptedViolationsParam);
            return grantApplicationActionService.applyApplication(versionId, data, fileDtoList, acceptedViolations);

        } catch (GryfOptimisticLockRuntimeException e) {
            grantApplicationActionService.manageLocking(id);
        }
        return null;
    }

    @RequestMapping(value = "/execute/{id}", method = RequestMethod.POST)
    public Long executeApplication(@PathVariable Long id,
                                   @RequestParam("data") String data,
                                   @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                   @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_PROC);
        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationActionService.executeApplication(id, data, fileDtoList, checkVatRegNumDup);

        } catch (GryfOptimisticLockRuntimeException e) {
            grantApplicationActionService.manageLocking(id, "Wystąpił konflikt przy zapisywaniu wniosku lub MŚP");
        }
        return null;
    }

    @RequestMapping(value = "/reject/{id}", method = RequestMethod.POST)
    public Long rejectApplication(@PathVariable Long id,
                                  @RequestParam("data") String data,
                                  @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATION_PROC);

        try {
            List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
            return grantApplicationActionService.rejectApplication(id, data, fileDtoList);

        } catch (GryfOptimisticLockRuntimeException e) {
            grantApplicationActionService.manageLocking(id, "Wystąpił konflikt przy zapisywaniu wniosku lub MŚP");
        }
        return null;
    }

    //PUBLIC MEHODS - DICTIONARIES

    @RequestMapping(value = "/grantProgramsDictionaries", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findGrantProgramsDictionaries() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationActionService.FindGrantProgramsDictionaries();
    }

    @RequestMapping(value = "/grantApplicationVersionsDictionaries/{grantProgramId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(@PathVariable Long grantProgramId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_APPLICATIONS);
        return grantApplicationActionService.findGrantApplicationVersionsDictionaries(grantProgramId);
    }

}
