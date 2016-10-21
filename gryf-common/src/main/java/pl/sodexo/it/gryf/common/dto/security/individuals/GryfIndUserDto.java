package pl.sodexo.it.gryf.common.dto.security.individuals;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Dto reprezentujące użytkownika osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
@ToString
public class GryfIndUserDto extends GryfIndUserHeadDto {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long indId;

    @Getter
    @Setter
    private Date lastLoginSuccessDate;

    @Getter
    @Setter
    private Date lastLoginFailureDate;

    @Getter
    @Setter
    private int loginFailureAttempts;

    @Getter
    @Setter
    private Date lastResetFailureDate;

    @Getter
    @Setter
    private int resetFailureAttempts;

}
