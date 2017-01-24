package pl.sodexo.it.gryf.common.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.user.UserVisitor;

import java.util.Collection;

/**
 * Uzytkownik GRYFa o roli Usługodawca (TI).
 *
 * Created by jbentyn on 2016-10-04.
 */
@ToString(callSuper = true)
public class GryfTiUser extends GryfUser {

    @Getter
    @Setter
    private Long trainingInstitutionId;

    public GryfTiUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.TRAINING_INSTITUTION;
    }

    @Override
    public <T> T accept(UserVisitor<T> userVisitor) {
        return userVisitor.visitTi(this);
    }
}
