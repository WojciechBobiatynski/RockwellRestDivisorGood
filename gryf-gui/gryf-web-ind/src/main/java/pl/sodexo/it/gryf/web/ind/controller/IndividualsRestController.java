package pl.sodexo.it.gryf.web.ind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.IndDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

/**
 * Created by adziobek on 20.10.2016.
 *
 * Kontroler do obługi panelu uczestnika.
 */
@RestController
@RequestMapping(value = "/rest/ind", produces = "application/json;charset=UTF-8")
public class IndividualsRestController {

    @Autowired
    IndividualService individualService;

    @Autowired
    TrainingInstanceService trainingInstanceService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndDto getIndividualAfterLogin() {
        return individualService.findIndividualAfterLogin();
    }

    @RequestMapping(value = "/reimbursmentPin/resend/{trainingInstanceId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void resendReimbursmentPin(@PathVariable("trainingInstanceId") Long trainingInstanceId) {
        securityChecker.assertIndUserAccessTrainingInstance(trainingInstanceId);
        trainingInstanceService.resendReimbursmentPin(trainingInstanceId);
    }
}