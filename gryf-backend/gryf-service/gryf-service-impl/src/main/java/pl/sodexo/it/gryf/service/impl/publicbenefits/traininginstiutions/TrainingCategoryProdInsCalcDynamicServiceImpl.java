package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproducts.PbeProductService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

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
    public Integer calculateProductInstanceForHour(TrainingReservationDto trainingReservationDto) {
        Training training = trainingRepository.get(trainingReservationDto.getTrainingId());
        TrainingCategoryParam trainingCategoryParam = paramInDateService.findTrainingCategoryParam(training.getCategory().getId(), training.getGrantProgram().getId(), new Date(),true);
        Integer maxBonProduct = trainingCategoryParam.getProductInstanceForHour();

        //If not found product then will be throw exception
        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(trainingReservationDto.getGrantProgramId(),
                GrantProgramProduct.Type.PRODUCT, trainingReservationDto.getStartDate(), true);

        //Value of 1 bon
        BigDecimal bonValue = gpProduct.getPbeProduct().getValue();

        BigDecimal trainingHourPrice = training.getHourPrice();

        // Number of bon calculated using hour price
        Integer numberCalculatedBon = trainingHourPrice.divide(bonValue, BigDecimal.ROUND_CEILING).intValue();

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
