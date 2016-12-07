package pl.sodexo.it.gryf.web.fo.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobSchedulerService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import java.util.List;
import java.util.Map;

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
        return asynchJobSchedulerService.findAsynchronousJobs(queryDTO);
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/dictionary/statuses", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getJobStatuses() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.getJobStatuses();
    }

    @RequestMapping(value = ASYNCH_JOBS_URL + "/dictionary/types", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getJobTypes() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        return asynchJobSchedulerService.getJobTypes();
    }

}
