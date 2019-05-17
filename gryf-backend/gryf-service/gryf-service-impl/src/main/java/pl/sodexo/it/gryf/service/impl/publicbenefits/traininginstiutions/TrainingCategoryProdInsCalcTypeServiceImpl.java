package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcType;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-11.
 */
@Service
public class TrainingCategoryProdInsCalcTypeServiceImpl implements TrainingCategoryProdInsCalcTypeService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private TrainingCategoryProdInsCalcTypeRepository trainingCategoryProdInsCalcTypeRepository;

    @Override
    public Integer getCalculateProductInstanceForHour(ProductCalculateDto productCalculateDto) {
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(productCalculateDto.getCategoryId(), productCalculateDto.getGrantProgramId(), productCalculateDto.getDate(),true);
        if(productCalculateDto.isIndividualTraining() && trainingCategoryParam.getIndividualProductInstance()!=null) {
            return trainingCategoryParam.getIndividualProductInstance();
        }
        TrainingCategoryProdInsCalcType trainingCategoryProdInsCalcType = trainingCategoryProdInsCalcTypeRepository.get(trainingCategoryParam.getProductInstanceCalcType().getId());
        TrainingCategoryProdInsCalcService trainingCategoryProdInsCalcService = (TrainingCategoryProdInsCalcService) BeanUtils.findBean(context, trainingCategoryProdInsCalcType.getService());
        return trainingCategoryProdInsCalcService.calculateProductInstanceForHour(productCalculateDto);
    }
}
