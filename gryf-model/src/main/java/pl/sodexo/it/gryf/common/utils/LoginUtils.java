package pl.sodexo.it.gryf.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
public abstract class LoginUtils {

    public static String getLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            //TODO zamienic na GryfUUser po rozdzieleniu commons i modelu
            User principal = (User) authentication.getPrincipal();
            if(principal != null){
                return principal.getUsername();
            }
        }
        return null;
    }
}
