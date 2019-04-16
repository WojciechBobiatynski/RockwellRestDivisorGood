package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.api.security.VerificationService;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

import static pl.sodexo.it.gryf.web.fo.utils.FoPageConstant.SEND_RESET_PASSWORD_LINK;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/trainingInstitution", produces = "application/json;charset=UTF-8")
public class TrainingInstitutionsRestController {

    @Autowired
    private TrainingInstitutionService trainingInstitutionService;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public TrainingInstitutionDto getTrainingInstitutionById() {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.createTrainingInstitution();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveTrainingInstitution(@RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
            @RequestBody TrainingInstitutionDto trainingInstitutionDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTION_MOD);
        return trainingInstitutionService.saveTrainingInstitution(trainingInstitutionDto, checkVatRegNumDup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingInstitutionDto getTrainingInstitutionById(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.findTrainingInstitution(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateTrainingInstitution(@PathVariable Long id,
                                          @RequestParam(value = "checkVatRegNumDup", required = false, defaultValue = "true") boolean checkVatRegNumDup,
            @RequestBody TrainingInstitutionDto trainingInstitutionDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTION_MOD);
        GryfUtils.checkForUpdate(id, trainingInstitutionDto.getId());

        trainingInstitutionService.updateTrainingInstitution(trainingInstitutionDto, checkVatRegNumDup);
        return trainingInstitutionDto.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO dto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        return trainingInstitutionService.findTrainingInstitutions(dto);

    }

    @RequestMapping(value = SEND_RESET_PASSWORD_LINK, method = RequestMethod.POST)
    public void sendResetPasswordLink(@RequestBody GryfTiUserDto gryfTiUserDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        verificationService.resetTiUserPassword(gryfTiUserDto.getEmail());
    }
}
