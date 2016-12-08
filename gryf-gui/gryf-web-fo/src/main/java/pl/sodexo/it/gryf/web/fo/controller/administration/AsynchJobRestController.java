package pl.sodexo.it.gryf.web.fo.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobSchedulerService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.common.util.WebUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = AsynchJobRestController.ADMINISTRATION_REST, produces = "application/json;charset=UTF-8")
public class AsynchJobRestController {

    public static final String ADMINISTRATION_REST = "rest/administration";
    public static final String ASYNCH_JOBS_URL = "/asynchjobs";

    @Autowired
    private AsynchJobSchedulerService asynchJobSchedulerService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = ASYNCH_JOBS_URL + "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<AsynchJobSearchResultDTO> findAsynchronousJobs(AsynchJobSearchQueryDTO queryDTO) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.findAsynchJobs(queryDTO);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/details/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    public AsynchJobDetailsDTO findAsynchJobDetails(@PathVariable("jobId") Long jobId) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.findAsynchJobDetails(jobId);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/dictionary/statuses", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getJobStatuses() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.getJobStatuses();
    }

    @RequestMapping(value = {ASYNCH_JOBS_URL + "/dictionary/types/{onlyImportJobs}",
                             ASYNCH_JOBS_URL + "/dictionary/types/{grantProgramId}/{onlyImportJobs}"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getJobTypes(@PathVariable("grantProgramId") Optional<Long> grantProgramId, @PathVariable("onlyImportJobs") boolean onlyImportJobs) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.getJobTypes(grantProgramId, onlyImportJobs);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/createImportJob", method = RequestMethod.POST)
    @ResponseBody
    public Long createImportJob(MultipartHttpServletRequest request) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        AsynchJobDetailsDTO jobDto = JsonMapperUtils.readValue(request.getParameter("data"), AsynchJobDetailsDTO.class);
        WebUtils.fillAsynchJobDtoWithAttachment(request.getFileMap(), jobDto);

        return asynchJobSchedulerService.saveAsynchronizeJob(jobDto);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/rows", method = RequestMethod.GET)
    @ResponseBody
    public List<ImportDataRowSearchResultDTO> findImportDataRows(ImportDataRowSearchQueryDTO queryDTO) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.findImportDataRows(queryDTO);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/rows/statuses", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getImportDataRowsStatuses() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.getImportDataRowsStatuses();
    }
}