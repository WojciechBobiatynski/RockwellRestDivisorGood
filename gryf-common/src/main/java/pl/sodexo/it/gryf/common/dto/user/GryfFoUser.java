package pl.sodexo.it.gryf.common.dto.user;

import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.user.UserVisitor;

import java.util.Collection;

/**
 * Uzytkownik GRYFa o roli OPERATOR FINANSOWY (FO).
 * 
 * Created by akuchna on 2016-09-28.
 */
public class GryfFoUser extends GryfUser {

    private static final long serialVersionUID = 1L;

    public GryfFoUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.FINANCIAL_OPERATOR;
    }

    @Override
    public <T> T accept(UserVisitor<T> userVisitor) {
        return userVisitor.visitFo(this);
    }

    @Override
    public String getAuditableInfo(){
        return String.format("%s:%s", userType.getCode(), getUsername());
    }

    @Override
    public String toString() {
        return "GryfFoUser[login=" + super.getUser().getLogin() + "]";
    }
}
