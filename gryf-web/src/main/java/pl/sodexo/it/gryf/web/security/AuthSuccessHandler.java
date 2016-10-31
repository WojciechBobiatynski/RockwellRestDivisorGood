package pl.sodexo.it.gryf.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pl.sodexo.it.gryf.common.dto.user.GryfFoUser;
import pl.sodexo.it.gryf.common.dto.user.GryfIndUser;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.user.UserVisitor;
import pl.sodexo.it.gryf.service.api.security.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Obsługa zdarzeia zalogowania użytkownika
 *
 * Created by jbentyn on 2016-10-07.
 */
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
        GryfUser user = (GryfUser) authentication.getPrincipal();
        saveLastLoginDate(user);
    }

    private void saveLastLoginDate(GryfUser gryfUser){
        gryfUser.accept(new UserVisitor<Object>() {

            @Override
            public Object visitFo(GryfFoUser gryfFoUser) {
                // nie zapisujemy daty ostatniego logowania
                return null;
            }

            @Override
            public Object visitInd(GryfIndUser gryfIndUser) {
                //TODO  data logowania dla IND
                return null;
            }

            @Override
            public Object visitTi(GryfTiUser gryfTiUser) {
                userService.updateTiAfterSuccessLogin(gryfTiUser);
                return null;
            }
        });
    }
}
