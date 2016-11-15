package pl.sodexo.it.gryf.common.dto.publicbenefits.products;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Dto zawierajace dane potrzebne do wygenerowania numeru drukowanego dla bonu
 *
 * Created by jbentyn on 2016-10-12.
 */
@ToString
@Getter
@Setter
public class PrintNumberDto {

    private String productId;

    private Integer typeNumber;

    private Integer faceValue;

    private Date validDate;

    private Long productInstanceNumber;

}
