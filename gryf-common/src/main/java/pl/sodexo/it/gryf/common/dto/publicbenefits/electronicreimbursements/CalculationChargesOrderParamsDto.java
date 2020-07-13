package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculationChargesOrderParamsDto {

    private Long orderId;

    private Integer orderUsedProductsNumber;

    private BigDecimal ownContributionPercentage;

    private BigDecimal indSubsidyPercentage;
}
