package pl.sodexo.it.gryf.common.dto.security.individuals;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.security.GryfBlockableUserDto;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.user.GryfBlockableUserVisitor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Dto reprezentujące użytkownika osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
@ToString
public class GryfIndUserDto extends GryfBlockableUserDto implements Serializable, Verifiable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long indId;

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
    private Date lastLoginSuccessDate;

    @Getter
    @Setter
    private Date lastResetFailureDate;

    @Getter
    @Setter
    private Integer resetFailureAttempts;

    @Getter
    @Setter
    private String verificationEmail;

    @Override
    public <T> T accept(GryfBlockableUserVisitor<T> userVisitor) {
        return userVisitor.visitInd(this);
    }

    @Override
    public String getLogin() {
        return pesel;
    }

    @Getter
    @Setter
    private Set<RoleDto> roles;
}
