package pl.sodexo.it.gryf.common.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;

import java.io.Serializable;

/**
 * Dto dla roli użytkowników
 *
 * Created by akmiecinski on 08.11.2016.
 */
@ToString
public class RoleDto extends GryfDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String context;

}
