package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/individual",
        produces = "application/json;charset=UTF-8")
//TODO uzycie encji
public class IndividualsRestController {

    @Autowired
    private IndividualService individualService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Individual getNewIndividual() {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.createIndividual();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveIndividual(@RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup,
                               @RequestBody Individual individual) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISE_MOD);
        individual = individualService.saveIndividual(individual, checkPeselDup);
        return individual.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Individual getIndividual(@PathVariable Long id) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividual(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateIndividual(@PathVariable Long id,
                                 @RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup,
                                 @RequestBody Individual individual) {
        //TODO uprawnienia
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, individual.getId());

        individualService.updateIndividual(individual, checkPeselDup);
        return individual.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO dto) {
        //TODO uprawnienia
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividuals(dto);

    }
}
