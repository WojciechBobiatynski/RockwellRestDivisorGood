package pl.sodexo.it.gryf.common.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SimpleDictionaryDto extends GryfDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Integer ordinal;

    @Getter
    @Setter
    private String name;
}
