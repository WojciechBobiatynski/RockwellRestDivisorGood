package pl.sodexo.it.gryf.common.dto.security.trainingInstitutions;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.security.GryfBlockableUserDto;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.user.GryfBlockableUserVisitor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto reprezentujące użytkownika instytucji szkoleniowej
 *
 * Created by akmiecinski on 24.10.2016.
 */
public class GryfTiUserDto extends GryfBlockableUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long trainingInstitutionId;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Date lastLoginSuccessDate;

    @Getter
    @Setter
    private Date passwordExpirationDate;

    @Getter
    @Setter
    private List<RoleDto> roles;

    @Override
    public <T> T accept(GryfBlockableUserVisitor<T> userVisitor) {
        return userVisitor.visitTi(this);
    }
}
