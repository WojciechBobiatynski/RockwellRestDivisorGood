package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-08.
 */
@Service(TrainingCategoryProdInsCalcConstansServiceImpl.CONSTANT_PRODUCT_INSTANCE)
public class TrainingCategoryProdInsCalcConstansServiceImpl implements TrainingCategoryProdInsCalcService {

    public static final String CONSTANT_PRODUCT_INSTANCE = "ConstantProductInstance";

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    public Integer calculateProductInstanceForHour(ProductCalculateDto productCalculateDto) {
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(productCalculateDto,true);
        return trainingCategoryParam.getProductInstanceForHour();
    }
}
