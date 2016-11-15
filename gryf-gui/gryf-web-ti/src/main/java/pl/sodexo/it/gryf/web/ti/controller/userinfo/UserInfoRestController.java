package pl.sodexo.it.gryf.web.ti.controller.userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;
@RestController
@RequestMapping(value = UrlConstants.USER_INFO_REST, produces = "application/json;charset=UTF-8")
public class UserInfoRestController {

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/trainingInstitution", method = RequestMethod.GET)
    public TrainingInstitutionDto getTrainingInstitutionOfLoggedUser() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        Long loggedUserInstitutionId = GryfUser.getLoggedTiUserInstitutionId();
        return trainingInstitutionService.findTrainingInstitution(loggedUserInstitutionId);
    }
}
