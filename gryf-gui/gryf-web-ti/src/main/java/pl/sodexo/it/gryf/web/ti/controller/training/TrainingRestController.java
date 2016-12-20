package pl.sodexo.it.gryf.web.ti.controller.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = UrlConstants.PATH_TRAINING_REST, produces = "application/json;charset=UTF-8")
public class TrainingRestController {

    @Autowired
    private TrainingService trainingService;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        return trainingService.findTrainingCategories();
    }

    @RequestMapping(value = "/listToReserve", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainingsToReserve(TrainingSearchQueryDTO dto) {
        dto.setInstitutionId(GryfUser.getLoggedTiUserInstitutionId());
        dto.setStartDateFrom(getStartDateFromToReservation(dto));
        dto.setActive(true);

        System.out.println("dto.getStartDateFrom()=" + dto.getStartDateFrom());
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        Long loggedUserInstitutionId = GryfUser.getLoggedTiUserInstitutionId();
        dto.setInstitutionId(loggedUserInstitutionId);
        return trainingService.findTrainings(dto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TrainingSearchResultDTO findTrainingById(@PathVariable("id") Long trainingId) {
        return trainingService.findTrainingOfInstitutionById(trainingId);
    }

    //PRIVATE METHODS

    private Date getStartDateFromToReservation(TrainingSearchQueryDTO dto){
        Date now = GryfUtils.getStartDay(new Date());
        if(dto.getStartDateFrom() == null){
            return now;
        }else if(dto.getStartDateFrom().before(new Date())){
            return now;
        }
        return dto.getStartDateFrom();
    }
}
