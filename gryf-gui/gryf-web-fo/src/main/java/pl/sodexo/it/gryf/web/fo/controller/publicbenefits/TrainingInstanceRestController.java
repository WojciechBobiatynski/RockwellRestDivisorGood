package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

/**
 * Kontroler dla instancji szkoleń
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

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

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
        return trainingInstanceService.findTrainingInstanceDetails(trainingInstanceId);
    }

    @RequestMapping(value = "/cancelTrainingReservation/{id}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("id") Long trainingInstanceId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES_MOD);
        productInstancePoolService.cancelTrainingInstance(trainingInstanceId);
    }

    @RequestMapping(value = "/confirmPin/{trainingInstanceId}/{pinCode}", method = RequestMethod.PUT)
    public void confirmPin(@PathVariable("trainingInstanceId") Long trainingInstanceId,
                           @PathVariable("pinCode") String pinCode) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES_MOD);
        productInstancePoolService.useTrainingInstance(trainingInstanceId, pinCode);
    }

}