package pl.sodexo.it.gryf.web.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sodexo.it.gryf.common.utils.GryfConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Abstrakcyjny handler dla nieudanych zalogowań
 *
 * Created by akmiecinski on 24.10.2016.
 */
public abstract class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl(GryfConstants.DEFAULT_FAILURE_LOGIN_URL);
        super.onAuthenticationFailure(request, response, exception);
        blockUserIfExceedLoginAttemptsCounter(request.getParameter(usernamePasswordAuthenticationFilter.getUsernameParameter()));
    }

    protected abstract void blockUserIfExceedLoginAttemptsCounter(String usernameParameter);

}
