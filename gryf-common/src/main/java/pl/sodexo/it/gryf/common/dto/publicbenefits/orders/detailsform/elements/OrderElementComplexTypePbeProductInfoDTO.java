package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Isolution on 2016-11-22.
 */
@ToString
public class OrderElementComplexTypePbeProductInfoDTO extends OrderElementDTO {

    @Getter
    @Setter
    private Integer productInstanceNum;

    @Getter
    @Setter
    private BigDecimal productInstanceAmount;

    @Getter
    @Setter
    private BigDecimal ownContributionPercen;

    @Getter
    @Setter
    private BigDecimal ownContributionAmont;

    @Getter
    @Setter
    private BigDecimal grantAmount;

    @Getter
    @Setter
    private BigDecimal orderAmount;
}
