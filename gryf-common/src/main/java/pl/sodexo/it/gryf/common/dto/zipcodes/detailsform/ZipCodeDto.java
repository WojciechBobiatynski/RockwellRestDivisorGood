package pl.sodexo.it.gryf.common.dto.zipcodes.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.VersionableDto;

/**
 * Dto dla encji ZipCode
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public class ZipCodeDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private StateDto state;

    @Getter
    @Setter
    private String zipCode;

    @Getter
    @Setter
    private String cityName;

    @Getter
    @Setter
    private Boolean active;

}
