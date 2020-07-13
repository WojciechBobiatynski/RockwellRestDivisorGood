package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla bonu/produktu.
 */
public class ProductHeadDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Date expirationDate;

    @Getter
    @Setter
    private Integer reservedProductsCount;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private BigDecimal ownContributionPercentage;

}