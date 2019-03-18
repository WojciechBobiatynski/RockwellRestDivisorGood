package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.util.Date;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-08.
 */
@Service(TrainingCategoryProdInsCalcConstansServiceImpl.CONSTANT_PRODUCT_INSTANCE)
public class TrainingCategoryProdInsCalcConstansServiceImpl implements TrainingCategoryProdInsCalcService {

    public static final String CONSTANT_PRODUCT_INSTANCE = "ConstantProductInstance";

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    public Integer calculateProductInstanceForHour(TrainingReservationDto trainingReservationDto) {
        Training training = trainingRepository.get(trainingReservationDto.getTrainingId());
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(training.getCategory().getId(), training.getGrantProgram().getId(), new Date(),true);
        return trainingCategoryParam.getProductInstanceForHour();
    }
}
