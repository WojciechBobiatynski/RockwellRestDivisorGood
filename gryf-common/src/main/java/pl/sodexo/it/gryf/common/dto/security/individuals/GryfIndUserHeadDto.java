package pl.sodexo.it.gryf.common.dto.security.individuals;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.io.Serializable;

/**
 * Dto reprezentujące nagłówek użytkownika osoby fizycznej na potrzeby autentykacji
 *
 * Created by akmiecinski on 19.10.2016.
 */
@ToString
public class GryfIndUserHeadDto extends VersionableDto implements Serializable{

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long inuId;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private String verificationCode;

    @Getter
    @Setter
    private boolean active;

}
