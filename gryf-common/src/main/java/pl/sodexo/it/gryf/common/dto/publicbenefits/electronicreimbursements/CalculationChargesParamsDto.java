package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Dto dla parametrów na podstawie których wyliczane są rozliczenia dla instancji szkolenia
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
    private BigDecimal productInstanceForHour;

    @Getter
    @Setter
    private BigDecimal maxProductInstance;

    @Getter
    @Setter
    private BigDecimal usedProductsNumber;

    @Getter
    @Setter
    private BigDecimal productValue;


}
