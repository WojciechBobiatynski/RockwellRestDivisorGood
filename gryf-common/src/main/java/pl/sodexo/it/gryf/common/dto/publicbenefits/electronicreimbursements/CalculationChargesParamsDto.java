package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Dto dla parametrów na podstawie których wyliczane są rozliczenia dla instancji usługi
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
public class CalculationChargesParamsDto {

    @Getter
    @Setter
    private BigDecimal trainingHourPrice;

    @Getter
    @Setter
    private Integer productInstanceForHour;

    @Getter
    @Setter
    private Integer maxProductInstance;

    @Getter
    @Setter
    private Integer usedProductsNumber;

    @Getter
    @Setter
    private BigDecimal productValue;

    @Getter
    @Setter
    private Integer trainingHoursNumber;

    @Getter
    @Setter
    private BigDecimal trainingPrice;

}
