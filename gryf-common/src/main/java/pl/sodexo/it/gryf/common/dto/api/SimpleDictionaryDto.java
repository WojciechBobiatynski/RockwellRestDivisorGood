package pl.sodexo.it.gryf.common.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ToString
public abstract class SimpleDictionaryDto extends GryfDto {

    @Getter
    @Setter
    private BigInteger ordinal;

    @Getter
    @Setter
    private String name;
}
