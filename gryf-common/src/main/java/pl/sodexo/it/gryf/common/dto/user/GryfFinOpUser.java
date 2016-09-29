package pl.sodexo.it.gryf.common.dto.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;

import java.util.Collection;

/**
 * Uzytkownik GRYFa o roli OPERATOR FINANSOWY.
 * 
 * Created by akuchna on 2016-09-28.
 */
@ToString(callSuper = true)
public class GryfFinOpUser extends GryfUser {

    public GryfFinOpUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.FINANCIAL_OPERATOR;
    }
}
