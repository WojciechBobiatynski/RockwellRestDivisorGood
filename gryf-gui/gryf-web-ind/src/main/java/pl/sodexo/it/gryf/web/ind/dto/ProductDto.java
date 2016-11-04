package pl.sodexo.it.gryf.web.ind.dto;

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
    private String orderId;

    @Getter
    @Setter
    private Integer grantedProductsCount;

    @Getter
    @Setter
    private Integer reservedProductsCount;

    @Getter
    @Setter
    private Integer usedProductsCount;

    @Getter
    @Setter
    private Integer availableProductsCount;

    @Getter
    @Setter
    private Date orderDate;

    @Getter
    @Setter
    private Date expirationDate;

    public ProductDto(Date expirationDate, Integer grantedProductsCount, Integer reservedProductsCount,
                      Integer availableProductsCount, Integer usedProductsCount, Date orderDate, String orderId) {
        this.setExpirationDate(expirationDate);
        this.setAvailableProductsCount(availableProductsCount);
        this.setGrantedProductsCount(grantedProductsCount);
        this.setOrderDate(orderDate);
        this.setOrderId(orderId);
        this.setReservedProductsCount(reservedProductsCount);
        this.setUsedProductsCount(usedProductsCount);
    }

}