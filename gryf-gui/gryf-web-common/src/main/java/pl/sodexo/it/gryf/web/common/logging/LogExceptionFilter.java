package pl.sodexo.it.gryf.web.common.logging;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filtr logujacy kazde niezlapane wczesniej Throwable.
 *
 * Created by adziobek on 28.09.2016.
 */
public class LogExceptionFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogExceptionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            LOGGER.error("Blad podczas obslugi", t);

            Throwables.propagateIfInstanceOf(t, RuntimeException.class);
            Throwables.propagateIfInstanceOf(t, IOException.class);
            Throwables.propagateIfInstanceOf(t, ServletException.class);
            throw new ServletException(t);
        }
    }

    @Override
    public void destroy() {
    }
}
