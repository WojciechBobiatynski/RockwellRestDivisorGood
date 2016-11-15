package pl.sodexo.it.gryf.common.dto.publicbenefits.products;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-11-15.
 */
@ToString
@Getter
@Setter
public class PrintNumberResultDto {

    private String generatedPrintNumber;

    private Integer generatedChecksum;

}
