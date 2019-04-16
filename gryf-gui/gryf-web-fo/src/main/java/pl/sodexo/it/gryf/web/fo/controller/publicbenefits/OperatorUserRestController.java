package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.config.ApplicationParametersNames;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.NoAppropriateData;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;
import pl.sodexo.it.gryf.web.fo.response.SimpleMessageResponse;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/fo", produces = "application/json;charset=UTF-8")
public class OperatorUserRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    @Autowired
    private TrainingInstitutionUserService trainingInstitutionUserService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @RequestMapping(value = "/userTi", method = RequestMethod.GET)
    public TrainingInstitutionSearchResultDTO getTrainingInstitutionForLoggedUser() {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        GryfTiUserDto tiUserDto = trainingInstitutionUserService.findTiUserForFoLogin(GryfUser.getLoggedUserLogin());
        return trainingInstitutionService.findTrainingInstitutionByUserLogin(tiUserDto.getLogin());
    }

    @RequestMapping(value = "/joinTi/{tiId}", method = RequestMethod.PUT)
    public GryfTiUserDto addLoggedUserToTrainingInstitution(@PathVariable Long tiId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        GryfTiUserDto tiUserDto = trainingInstitutionUserService.findTiUserForFoLogin(GryfUser.getLoggedUserLogin());
        tiUserDto.setTrainingInstitutionId(tiId);
        return trainingInstitutionUserService.saveTiUser(tiUserDto);
    }

    @RequestMapping(value = "/resetTi", method = RequestMethod.PUT)
    public GryfTiUserDto resetLoggedUserTrainingInstitution() {
        String param = applicationParameters.findParameterValueByCode(
                ApplicationParametersNames.DEFAULT_TI.toString());
        return addLoggedUserToTrainingInstitution(Long.parseLong(param));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NoAppropriateData.class)
    public SimpleMessageResponse handleNoTiUserFound(NoAppropriateData e) {
        return new SimpleMessageResponse(e.getMessage());
    }
}
