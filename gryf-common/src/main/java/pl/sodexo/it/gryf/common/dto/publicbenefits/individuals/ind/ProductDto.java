package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla bonu/produktu.
 */
public class ProductDto extends ProductHeadDto {

    @Getter
    @Setter
    private Integer grantedProductsCount;

    @Getter
    @Setter
    private Integer availableProductsCount;

    @Getter
    @Setter
    private Integer usedProductsCount;

    @Getter
    @Setter
    private Date orderDate;
}