package pl.sodexo.it.gryf.common.dto.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import pl.sodexo.it.gryf.common.dto.security.UserDto;
import pl.sodexo.it.gryf.common.user.UserVisitor;

import java.util.Collection;

/**
 * Uzytkownik GRYFa o roli OSOBA FIZYCZNA (IND).
 *
 * Created by akuchna on 2016-10-14.
 */
@ToString(callSuper = true)
public class GryfIndUser extends GryfUser {

    private Long individualId;

    public GryfIndUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user, authorities);
        userType = UserType.INDIVIDUAL;
    }

    @Override
    public <T> T accept(UserVisitor<T> userVisitor) {
        return userVisitor.visitInd(this);
    }

    @Override
    public String getAuditableInfo(){
        return String.format("%s:%s", userType.getCode(), individualId);
    }

    public Long getIndividualId() {
        return individualId;
    }

    public void setIndividualId(Long individualId) {
        this.individualId = individualId;
    }
}
