package pl.sodexo.it.gryf.web.ind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.IndDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

/**
 * Created by adziobek on 20.10.2016.
 *
 * Kontroler do obługi panelu uczestnika.
 */
@RestController
@RequestMapping(value = "/rest/ind", produces = "application/json;charset=UTF-8")
public class IndividualsRestController {

    @Autowired
    IndividualService individualService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndDto getIndividualAfterLogin() {
        securityChecker.assertServicePrivilege(Privileges.GRF_INDIVIDUALS);
        return individualService.findIndividualAfterLogin();
    }

}