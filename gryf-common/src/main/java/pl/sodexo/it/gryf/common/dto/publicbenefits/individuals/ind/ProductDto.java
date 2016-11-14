package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla bonu/produktu.
 */
public class ProductDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Date expirationDate;

    @Getter
    @Setter
    private Integer grantedProductsCount;

    @Getter
    @Setter
    private Integer reservedProductsCount;

    @Getter
    @Setter
    private Integer availableProductsCount;

    @Getter
    @Setter
    private Integer usedProductsCount;

    @Getter
    @Setter
    private Date orderDate;

    @Getter
    @Setter
    private String orderId;

}