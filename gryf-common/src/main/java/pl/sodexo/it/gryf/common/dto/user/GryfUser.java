package pl.sodexo.it.gryf.common.dto.user;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import pl.sodexo.it.gryf.common.dto.security.UserDto;

import java.util.Collection;

/**
 * Zalogowany uzytkownik jest reprezentowany przez ten obiekt w kontekscie spring security.
 *
 * Created by adziobek on 28.09.2016.
 */
@Getter
@ToString
public abstract class GryfUser extends User {

    private static final long serialVersionUID = 1L;

    protected UserType userType = null;

    protected UserDto user;

    public GryfUser(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getLogin(), user.getLogin(), authorities);
        this.user = user;
    }

    public String getUserType() {
        if (userType == null) return "NO_TYPE";
        return userType.toString();
    }

    /**
     * Zwraca login zalogowanego użytkownika.
     *
     * @return login
     */
    public static String getLoggedUserLogin() {
        GryfUser loggedUser = GryfUser.getLoggedUser();
        if (loggedUser == null){
            return "";
        }

        UserDto user = loggedUser.getUser();
        if (user == null){
            return "";
        }

        return user.getLogin();
    }
    
    /**
     * Zwraca zalogowanego użytkownika.
     *
     * @return zalogowany uzytkownik
     */
    public static GryfUser getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getGryfUser(authentication);
    }

    /**
     * Zwraca zalogowanego użytkownika dla przekazanego obiektu autentykacji.
     *
     * @return zalogowany uzytkownik
     */
    public static GryfUser getGryfUser(Authentication authentication) {
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof GryfUser)) {
            return null;
        }
        return (GryfUser) principal;
    }
    
}