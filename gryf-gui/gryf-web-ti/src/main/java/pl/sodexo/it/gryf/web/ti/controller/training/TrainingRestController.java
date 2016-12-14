package pl.sodexo.it.gryf.web.ti.controller.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_REST, produces = "application/json;charset=UTF-8")
public class TrainingRestController {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTrainingCategories();
    }

    @RequestMapping(value = "/listToReserve", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainingsToReserve(TrainingSearchQueryDTO dto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        Long loggedUserInstitutionId = GryfUser.getLoggedTiUserInstitutionId();
        dto.setInstitutionId(loggedUserInstitutionId);
        dto.setActive(true);
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        Long loggedUserInstitutionId = GryfUser.getLoggedTiUserInstitutionId();
        dto.setInstitutionId(loggedUserInstitutionId);
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingSearchResultDTO findTrainingById(@PathVariable("id") Long trainingId) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTrainingOfInstitutionById(trainingId);
    }
}
