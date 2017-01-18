package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_INTEGER_SCALE;

/**
 * Created by kantczak on 2017-01-17.
 */
public class ProductCalculationsDto {

    @Getter
    @Setter
    private Integer productInstanceForHour;

    @Getter
    @Setter
    private Integer maxProductInstance;

    @Getter
    @Setter
    private BigDecimal prdValue;

    protected Integer getMaxProductsNumber(BigDecimal trainingPrice, Integer trainingHoursNumber) {
        Integer maxProductsNumber;
        if(maxProductInstance != null) {
            if(isTrainingPriceLowerThanMaxProgramLimit(trainingPrice)){
                BigDecimal result = trainingPrice.divide(prdValue, BIG_DECIMAL_INTEGER_SCALE, RoundingMode.UP);
                maxProductsNumber = result.intValue();
            } else {
                maxProductsNumber = maxProductInstance;
            }
        } else {
            maxProductsNumber = productInstanceForHour * trainingHoursNumber;
        }
        return maxProductsNumber;
    }

    private boolean isTrainingPriceLowerThanMaxProgramLimit(BigDecimal trainingPrice) {
        return trainingPrice.compareTo(prdValue.multiply(BigDecimal.valueOf(maxProductInstance))) < 0;
    }
}
