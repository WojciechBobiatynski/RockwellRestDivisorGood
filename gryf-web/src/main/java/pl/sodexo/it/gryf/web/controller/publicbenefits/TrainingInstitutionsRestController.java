package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.SecurityCheckerService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/trainingInstitution", produces = "application/json;charset=UTF-8")
//TODO uzycie encji
public class TrainingInstitutionsRestController {

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    @Autowired
    private SecurityCheckerService securityCheckerService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public TrainingInstitution getTrainingInstitutionById() {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.createTrainingInstitution();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveTrainingInstitution(@RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                        @RequestBody TrainingInstitution trainingInstitution) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTION_MOD);
        trainingInstitution = trainingInstitutionService.saveTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        return trainingInstitution.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingInstitution getTrainingInstitutionById(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.findTrainingInstitution(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateTrainingInstitution(@PathVariable Long id,
                                          @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
                                          @RequestBody TrainingInstitution trainingInstitution) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTION_MOD);
        GryfUtils.checkForUpdate(id, trainingInstitution.getId());

        trainingInstitutionService.updateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        return trainingInstitution.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO dto) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.findTrainingInstitutions(dto);

    }
}
