package pl.sodexo.it.gryf.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
public abstract class LoginUtils {

    public static String getLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            Object principal = authentication.getPrincipal();
            if(principal != null){
                return principal.toString();
            }
        }
        return null;
    }
}
