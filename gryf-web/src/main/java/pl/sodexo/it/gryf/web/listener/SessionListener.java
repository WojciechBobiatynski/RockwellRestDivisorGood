package pl.sodexo.it.gryf.web.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
public class SessionListener implements HttpSessionListener {

    //PUBLIC METHODS

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        int sessionTimeout = getSessionTimeout(event);
        event.getSession().setMaxInactiveInterval(60 * sessionTimeout);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    }

    //PRIVATE METHODS

    private int getSessionTimeout(HttpSessionEvent sessionEvent){
        HttpSession session = sessionEvent.getSession();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        ApplicationParameters applicationParameters = (ApplicationParameters) ctx.getBean("applicationParameters");
        return applicationParameters.getSessionTimeout();
    }

}