package pl.sodexo.it.gryf.web.ti.controller.programs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.PATH_PROGRAMS_REST, produces = "application/json;charset=UTF-8")
public class ProgramRestController {

    @Autowired
    private ContractService contractService;


    @RequestMapping(value = UrlConstants.PATH_PROGRAMS_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<GrantProgramDictionaryDTO> findGrantProgramNames() {
        return contractService.findGrantProgramsDictionaries();
    }
}
