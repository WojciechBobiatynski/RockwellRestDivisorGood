package pl.sodexo.it.gryf.common.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.user.GryfBlockableUserVisitor;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto reprezentujące użytkowników, którzy mogą być zablokowaniu po kilku nieudanych próbach logowania
 *
 * Created by akmiecinski on 24.10.2016.
 */
@ToString
public abstract class GryfBlockableUserDto extends VersionableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private Date lastLoginFailureDate;

    @Getter
    @Setter
    private int loginFailureAttempts;

    public abstract <T> T accept(GryfBlockableUserVisitor<T> userVisitor);
}
