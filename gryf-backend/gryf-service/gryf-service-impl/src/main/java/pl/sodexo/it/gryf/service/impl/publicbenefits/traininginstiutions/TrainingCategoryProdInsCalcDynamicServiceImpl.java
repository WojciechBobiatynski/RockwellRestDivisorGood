package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.math.BigDecimal;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-08.
 */
@Service(TrainingCategoryProdInsCalcDynamicServiceImpl.DYNAMIC_PRODUCT_INSTANCE)
public class TrainingCategoryProdInsCalcDynamicServiceImpl implements TrainingCategoryProdInsCalcService {

    public static final String DYNAMIC_PRODUCT_INSTANCE = "DynamicProductInstance";

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    public Integer calculateProductInstanceForHour(ProductCalculateDto productCalculateDto) {
        Training training = trainingRepository.get(productCalculateDto.getTrainingId());
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(productCalculateDto,true);
        Integer maxBonProduct = trainingCategoryParam.getProductInstanceForHour();

        //If not found product then will be throw exception
        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(productCalculateDto.getGrantProgramId(),
                GrantProgramProduct.Type.PBE_PRODUCT, productCalculateDto.getDate(), true);

        //Value of 1 bon
        BigDecimal bonValue = gpProduct.getPbeProduct().getValue();

        BigDecimal trainingHourPrice = training.getHourPrice();

        // Number of bon calculated using hour price
        Integer numberCalculatedBon = trainingHourPrice.divide(bonValue, 0, BigDecimal.ROUND_CEILING).intValue();

        /**
         *  Algorithm:
         *  <li>If number of calculated bon is equal or less than maxBonProduct then</li>
         *  <li>otherwise - return max number of bon defined by System</li>
         *
         */
        if (numberCalculatedBon.compareTo(maxBonProduct) <= 0 ) {
            return numberCalculatedBon.intValue();
        } else {
            return maxBonProduct;
        }

    }

}
