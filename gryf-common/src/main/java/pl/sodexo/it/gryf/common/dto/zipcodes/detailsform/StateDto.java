package pl.sodexo.it.gryf.common.dto.zipcodes.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;

/**
 * Dto dla encji State
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public class StateDto extends GryfDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Long country;

}
