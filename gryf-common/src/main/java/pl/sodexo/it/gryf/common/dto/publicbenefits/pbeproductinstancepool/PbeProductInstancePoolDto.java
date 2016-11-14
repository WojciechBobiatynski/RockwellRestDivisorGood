package pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;

/**
 * Klasa bazowa dla dto klas kontaktów
 *
 * Created by jbentyn on 2016-10-04.
 */
@ToString
public class PbeProductInstancePoolDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Date expiryDate;

    @Getter
    @Setter
    private Integer availableNum;
}
