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

    //PRIVATE FIELDS

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    @Autowired
    private SecurityChecker securityChecker;

    //PUBLIC METHODS - FINDS

    @RequestMapping(value = "/userTrainingReservationData", method = RequestMethod.POST)
    public UserTrainingReservationDataDto findUserTrainingReservationData(@RequestBody IndUserAuthDataDto userAuthDataDto) {
        return productInstancePoolService.findUserTrainingReservationData(userAuthDataDto);
    }

    //PUBLIC METHODS - ACTIONS

    @RequestMapping(value = "/reserveTraining", method = RequestMethod.POST)
    public void reserveTraining(@RequestBody TrainingReservationDto reservationDto) {
        securityChecker.assertTiUserAccessTraining(reservationDto.getTrainingId());
        trainingInstanceService.createTrainingInstance(reservationDto);
    }

    @RequestMapping(value = "/cancelTrainingReservation/{id}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("id") Long trainingInstanceId) {
        securityChecker.assertTiUserAccessTrainingInstance(trainingInstanceId);
        trainingInstanceService.cancelTrainingInstance(trainingInstanceId);
    }

    @RequestMapping(value = "/confirmPin/{trainingInstanceId}", method = RequestMethod.PUT)
    public void confirmPin(@PathVariable("trainingInstanceId") Long trainingInstanceId, @RequestBody String pinCode) {
        securityChecker.assertTiUserAccessTrainingInstance(trainingInstanceId);
        trainingInstanceService.useTrainingInstance(trainingInstanceId, pinCode);
    }
}
