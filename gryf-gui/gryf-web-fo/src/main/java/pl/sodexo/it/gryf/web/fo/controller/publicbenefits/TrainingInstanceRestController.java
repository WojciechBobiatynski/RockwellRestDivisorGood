package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceUseDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

/**
 * Kontroler dla instancji usług
 *
 * Created by akmiecinski on 15.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_INSTANCE, produces = "application/json;charset=UTF-8")
public class TrainingInstanceRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    @RequestMapping(value = PATH_TRAINING_INSTANCE_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<TrainingInstanceDto> findTrainingInstancesByCriteria(TrainingInstanceCriteria trainingInstanceCriteria){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES);
        return trainingInstanceService.findTrainingInstanceListByCriteria(trainingInstanceCriteria);
    }

    @RequestMapping(value = PATH_TRAINING_INSTANCE_STATUSES_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses(){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES);
        return trainingInstanceService.findTrainingInstanceStatuses();
    }

    @RequestMapping(value = PATH_TRAINING_INSTANCE_DETAILS_FIND + "/{trainingInstanceId}", method = RequestMethod.GET)
    @ResponseBody
    public TrainingInstanceDetailsDto findTrainingDetails(@PathVariable Long trainingInstanceId){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES);
        return trainingInstanceService.findTrainingInstanceDetailsWithPinCode(trainingInstanceId);
    }

    @RequestMapping(value = "/cancelTrainingReservation/{id}/{version}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("id") Long trainingInstanceId, @PathVariable("version") Integer version) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES_MOD);
        trainingInstanceService.cancelTrainingInstance(trainingInstanceId, version);
    }

    @RequestMapping(value = "/confirmPin", method = RequestMethod.PUT)
    public void confirmPin(@RequestBody TrainingInstanceUseDto useDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES_MOD);
        trainingInstanceService.useTrainingInstance(useDto);
    }

}