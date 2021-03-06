package pl.sodexo.it.gryf.common.dto.publicbenefits;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;

/**
 * Dto dla encji ContactType
 *
 * Created by jbentyn on 2016-09-26.
 */
@ToString
public class ContactTypeDto extends GryfDto {

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String validationClass;
}
