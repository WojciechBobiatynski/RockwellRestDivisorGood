package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcType;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.util.Date;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-11.
 */
@Service
public class TrainingCategoryProdInsCalcTypeServiceImpl implements TrainingCategoryProdInsCalcTypeService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private TrainingCategoryProdInsCalcTypeRepository trainingCategoryProdInsCalcTypeRepository;

    @Override
    public Integer getCalculateProductInstanceForHour(TrainingReservationDto trainingReservationDto) {
        Training training = trainingRepository.get(trainingReservationDto.getTrainingId());
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(training.getCategory().getId(), training.getGrantProgram().getId(), new Date(),true);
        TrainingCategoryProdInsCalcType trainingCategoryProdInsCalcType = trainingCategoryProdInsCalcTypeRepository.get(trainingCategoryParam.getProductInstanceCalcType().getId());
        TrainingCategoryProdInsCalcService trainingCategoryProdInsCalcService = (TrainingCategoryProdInsCalcService) BeanUtils.findBean(context, trainingCategoryProdInsCalcType.getService());
        return trainingCategoryProdInsCalcService.calculateProductInstanceForHour(trainingReservationDto);
    }
}
