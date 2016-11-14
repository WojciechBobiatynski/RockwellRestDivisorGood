package pl.sodexo.it.gryf.web.ti.controller.trainingreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.TRAINING_RESERVATION_REST, produces = "application/json;charset=UTF-8")
public class TrainingReservationRestController {

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/availableProductPool", method = RequestMethod.POST)
    public List<PbeProductInstancePoolDto> getTrainingInstitutionOfLoggedUser(@RequestBody IndUserAuthDataDto userAuthDataDto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return productInstancePoolService.findProductInstancePoolsOfUser(userAuthDataDto);
    }
}
