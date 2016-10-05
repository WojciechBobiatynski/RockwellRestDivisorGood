package pl.sodexo.it.gryf.common.dto.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;

import java.util.Collection;

/**
 * Created by jbentyn on 2016-10-04.
 */
@ToString(callSuper = true)
public class GryfTrInUser extends GryfUser {

    public GryfTrInUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.TRAINING_INSTITUTION;
    }
}
