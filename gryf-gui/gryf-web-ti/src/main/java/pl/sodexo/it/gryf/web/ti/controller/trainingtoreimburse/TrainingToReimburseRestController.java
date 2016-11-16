package pl.sodexo.it.gryf.web.ti.controller.trainingtoreimburse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.criteria.trainingtoreimburse.TrainingToReimburseCriteria;
import pl.sodexo.it.gryf.common.dto.trainingtoreimburse.TrainingToReimburseDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.ti.util.UrlConstants.PATH_TRAINING_TO_REIMBURSE_LIST;

/**
 * Kontroler dla szkoleń do rozliczenia
 *
 * Created by akmiecinski on 15.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_TO_REIMBURSE_REST, produces = "application/json;charset=UTF-8")
public class TrainingToReimburseRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private TrainingService trainingService;

    @RequestMapping(value = PATH_TRAINING_TO_REIMBURSE_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<TrainingToReimburseDto> findElctRmbsByCriteria(TrainingToReimburseCriteria trainingToReimburseCriteria){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return trainingService.findTrainingToReimburseListByCriteria(trainingToReimburseCriteria);
    }

//    @RequestMapping(value = PATH_TRAINING_TO_REIMBURSE_STATUSES_LIST, method = RequestMethod.GET)
//    @ResponseBody
//    public List<SimpleDictionaryDto> findElctRmbsStatuses(){
//        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
//        return electronicReimbursementsService.findElctRmbsStatuses();
//    }

}
