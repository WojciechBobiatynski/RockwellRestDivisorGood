package pl.sodexo.it.gryf.common.dto.user;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import pl.sodexo.it.gryf.common.dto.security.UserDto;

/**
 * Zalogowany uzytkownik jest reprezentowany przez ten obiekt w kontekscie spring security.
 *
 * Created by adziobek on 28.09.2016.
 */
@Data
public abstract class GryfUser extends User {

    private static final long serialVersionUID = 1L;

    protected UserType userType = null;

    protected UserDto user;

    protected String currentRoleCode;

    public GryfUser(UserDto user, String currentRole) {
        super(user.getName(), user.getLogin(), null);
        this.user = user;
        this.currentRoleCode = currentRole;
    }

    public String getUserType() {
        if (userType == null) return "NO_TYPE";
        return userType.toString();
    }

    public static GryfUser getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getGryfUser(authentication);
    }

    public static GryfUser getGryfUser(Authentication authentication) {
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof GryfUser)) {
            return null;
        }
        return (GryfUser) principal;
    }

    public String getFullName() {
        if (user == null) {
            return "";
        }
        return user.getName() + " " + user.getSurname();
    }

    public String getUserLogin(){
        if(user==null){
            return "";
        }
        return user.getLogin();
    }

    public GryfUser getOriginalIdentity(){
        return null;
    }
}