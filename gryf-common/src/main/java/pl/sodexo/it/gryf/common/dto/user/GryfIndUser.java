package pl.sodexo.it.gryf.common.dto.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;

import java.util.Collection;

/**
 * Uzytkownik GRYFa o roli OSOBA FIZYCZNA (IND).
 *
 * Created by akuchna on 2016-10-14.
 */
@ToString(callSuper = true)
public class GryfIndUser extends GryfUser {

    public GryfIndUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.INDIVIDUAL;
    }
}
