package pl.sodexo.it.gryf.web.ti.controller.trainingreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_RESERVATION_REST, produces = "application/json;charset=UTF-8")
public class TrainingReservationRestController {

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private SecurityChecker securityChecker;
    //TODO: security checker for all requests

    @RequestMapping(value = "/userTrainingReservationData", method = RequestMethod.POST)
    public UserTrainingReservationDataDto findUserTrainingReservationData(@RequestBody IndUserAuthDataDto userAuthDataDto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return productInstancePoolService.findUserTrainingReservationData(userAuthDataDto);
    }

    @RequestMapping(value = "/trainingCategoriesDict", method = RequestMethod.GET)
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTrainingCategories();
    }

    @RequestMapping(value = "/training/list", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        Long loggedUserInstitutionId = GryfUser.getLoggedTiUserInstitutionId();
        dto.setInstitutionId(loggedUserInstitutionId);
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/training/{id}", method = RequestMethod.GET)
    public TrainingSearchResultDTO findTrainingById(@PathVariable("id") Long trainingId) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        return trainingService.findTrainingOfInstitutionById(trainingId);
    }

    @RequestMapping(value = "/reserveTraining", method = RequestMethod.POST)
    public void reserveTraining(@RequestBody TrainingReservationDto reservationDto) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        productInstancePoolService.createTrainingInstance(reservationDto);
    }

    @RequestMapping(value = "/cancelTrainingReservation/{id}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("id") Long trainingInstanceId) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        productInstancePoolService.cancelTrainingInstance(trainingInstanceId);
    }

    @RequestMapping(value = "/confirmPin/{trainingInstanceId}/{pinCode}", method = RequestMethod.PUT)
    public void cancelTrainingReservation(@PathVariable("trainingInstanceId") Long trainingInstanceId,
                                          @PathVariable("pinCode") String pinCode) {
        //securityChecker.assertServicePrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        productInstancePoolService.useTrainingInstance(trainingInstanceId, pinCode);
    }
}
