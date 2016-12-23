package pl.sodexo.it.gryf.web.ti.controller.traininginstance;

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
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.ti.util.UrlConstants.PATH_TRAINING_INSTANCE_LIST;
import static pl.sodexo.it.gryf.web.ti.util.UrlConstants.PATH_TRAINING_INSTANCE_DETAILS;
import static pl.sodexo.it.gryf.web.ti.util.UrlConstants.PATH_TRAINING_INSTANCE_STATUSES_LIST;

/**
 * Kontroler dla instancji szkoleń
 *
 * Created by akmiecinski on 15.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_INSTANCE_REST, produces = "application/json;charset=UTF-8")
public class TrainingInstanceRestController {

    //PRIVATE FIELDS

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    //PUBLIC METHODS - FIND

    @RequestMapping(value = PATH_TRAINING_INSTANCE_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<TrainingInstanceDto> findTrainingInstancesByCriteria(TrainingInstanceCriteria trainingInstanceCriteria){
        return trainingInstanceService.findTrainingInstanceListByCriteria(trainingInstanceCriteria);
    }

    @RequestMapping(value = PATH_TRAINING_INSTANCE_DETAILS + "/{trainingInstanceId}", method = RequestMethod.GET)
    @ResponseBody
    public TrainingInstanceDetailsDto findTrainingInstanceDetails(@PathVariable Long trainingInstanceId){
        securityChecker.assertTiUserAccessTrainingInstance(trainingInstanceId);
        return trainingInstanceService.findTrainingInstanceDetails(trainingInstanceId);
    }

    //PUBLIC METHODS - OTHERS

    @RequestMapping(value = PATH_TRAINING_INSTANCE_STATUSES_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses(){
        return trainingInstanceService.findTrainingInstanceStatuses();
    }
}
