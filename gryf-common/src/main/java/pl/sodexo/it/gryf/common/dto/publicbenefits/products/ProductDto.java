package pl.sodexo.it.gryf.common.dto.publicbenefits.products;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Dto dla produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
@ToString
@Getter
@Setter
public class ProductDto implements Serializable {

    private String productId;

    private String name;

}
