package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;


@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/training", produces = "application/json;charset=UTF-8")
public class TrainingRestController {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private SecurityChecker securityChecker;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public TrainingDTO getTrainingById() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.createTraining();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveTraining(@RequestBody TrainingDTO trainingDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS_MOD);
        return trainingService.saveTraining(trainingDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingDTO getTrainingById(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTraining(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateTraining(@PathVariable Long id, @RequestBody TrainingDTO trainingDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS_MOD);
        GryfUtils.checkForUpdate(id, trainingDto.getTrainingId());

        trainingService.updateTraining(trainingDto);
        return trainingDto.getTrainingId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/getTrainingCategoriesDict", method = RequestMethod.GET)
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        Long grantProgramId = 100L;
        trainingService.findTrainingCategoriesByGrantProgram(grantProgramId);
        return trainingService.getTrainingCategoriesDict();
    }
}
