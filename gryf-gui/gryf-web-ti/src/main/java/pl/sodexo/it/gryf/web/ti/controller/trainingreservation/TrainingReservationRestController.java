package pl.sodexo.it.gryf.web.ti.controller.trainingreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_RESERVATION_REST, produces = "application/json;charset=UTF-8")
public class TrainingReservationRestController {

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/userTrainingReservationData", method = RequestMethod.POST)
    public UserTrainingReservationDataDto findUserTrainingReservationData(@RequestBody IndUserAuthDataDto userAuthDataDto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return productInstancePoolService.findUserTrainingReservationData(userAuthDataDto);
    }

    @RequestMapping(value = "/reserveTraining", method = RequestMethod.POST)
    public void reserveTraining(@RequestBody TrainingReservationDto reservationDto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        trainingInstanceService.createTrainingInstance(reservationDto);
    }

    @RequestMapping(value = "/cancelTrainingReservation/{id}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("id") Long trainingInstanceId) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        trainingInstanceService.cancelTrainingInstance(trainingInstanceId);
    }

    @RequestMapping(value = "/confirmPin/{trainingInstanceId}/{pinCode}", method = RequestMethod.PUT)
    public void confirmPin(@PathVariable("trainingInstanceId") Long trainingInstanceId,
                                          @PathVariable("pinCode") String pinCode) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        trainingInstanceService.useTrainingInstance(trainingInstanceId, pinCode);
    }
}
