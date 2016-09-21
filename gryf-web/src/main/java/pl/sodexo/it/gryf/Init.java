package pl.sodexo.it.gryf;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import pl.sodexo.it.gryf.web.SessionListener;
import pl.sodexo.it.gryf.web.WebContext;
import pl.sodexo.it.gryf.web.security.SecurityConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class Init extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{BaseContext.class, SecurityConfig.class};
    } 

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebContext.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
    }
}
