package pl.sodexo.it.gryf.web.ti.controller.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingPrecalculatedDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_REST, produces = "application/json;charset=UTF-8")
public class TrainingRestController {

    //PRIVATE FIELDS

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ApplicationParameters applicationParameters;

    //PUBLIC METHODS - FINDS

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/listToReserve/{grantProgramId}/{indId}", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainingsToReserve(@PathVariable("grantProgramId") Long grantProgramId, @PathVariable("indId") Long indId, TrainingSearchQueryDTO dto) {
        dto.setStartDateFrom(trainingInstanceService.findReservationDatePossibility(grantProgramId, new Date()));
        dto.setActive(true);
        dto.setIndividualId(indId);
        return trainingService.findTrainingsByProgramIdAndIndividualIdUsingContractsIds(dto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingSearchResultDTO findTrainingDetails(@PathVariable("id") Long trainingId) {
        securityChecker.assertTiUserAccessTraining(trainingId);
        return trainingService.findTrainingDetails(trainingId);
    }

    @RequestMapping(value = "/precalculated/{id}/{grantProgramId}", method = RequestMethod.GET)
    public TrainingPrecalculatedDetailsDto findTrainingPrecalculatedDetails(@PathVariable("id") Long trainingId,
                                                                            @PathVariable("grantProgramId") Long grantProgramId) {
        securityChecker.assertTiUserAccessTraining(trainingId);
        return trainingService.findTrainingPrecalculatedDetails(trainingId, grantProgramId);
    }

    //PUBLIC METHODS - OTHERS

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        return trainingService.findTrainingCategories();
    }

    @RequestMapping(value = "/feedbackEmail", method = RequestMethod.GET)
    public List<String> getFeedbackEmail() {
        List<String> emails = new ArrayList<>();
        emails.add(applicationParameters.getGryfPbeDefPubEmailReplyTo());
        return emails;
    }

    //PRIVATE METHODS

    private Date getStartDateFromToReservation(TrainingSearchQueryDTO dto) {
        Date nextDay = GryfUtils.getStartDay(GryfUtils.addDays(new Date(), 1));
        if (dto.getStartDateFrom() == null) {
            return nextDay;
        } else if (dto.getStartDateFrom().before(nextDay)) {
            return nextDay;
        }
        return dto.getStartDateFrom();
    }
}