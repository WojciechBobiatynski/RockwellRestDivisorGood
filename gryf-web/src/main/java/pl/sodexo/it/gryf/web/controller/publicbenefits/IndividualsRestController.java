package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/individual",
        produces = "application/json;charset=UTF-8")
public class IndividualsRestController {

    @Autowired
    private IndividualService individualService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndividualDto getNewIndividual() {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.createIndividual();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveIndividual(@RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup, @RequestBody IndividualDto individualDto) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISE_MOD);
        individualDto = individualService.saveIndividual(individualDto, checkPeselDup);
        return individualDto.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IndividualDto getIndividual(@PathVariable Long id) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividual(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateIndividual(@PathVariable Long id,
                                 @RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup, @RequestBody IndividualDto individualDto) {
        //TODO uprawnienia
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, individualDto.getId());

        individualService.updateIndividual(individualDto, checkPeselDup);
        return individualDto.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO dto) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividuals(dto);

    }
}
