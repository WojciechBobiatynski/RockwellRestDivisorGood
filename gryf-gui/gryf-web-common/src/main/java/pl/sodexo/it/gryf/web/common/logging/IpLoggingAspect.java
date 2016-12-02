package pl.sodexo.it.gryf.web.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Aspekt logujący do pliku informacje o IP użytkowników dla IND
 *
 * Created by akmiecinski on 01.12.2016.
 */
@Aspect
public class IpLoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger("ip." + IpLoggingAspect.class.getName());

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    private static final String LOGGER_USER_LOG_FILENAME_PLACEHOLDER = "user_role";
    private static final String LOGGER_TI_USER_LOG_FILENAME = "gryf-ti";
    private static final String LOGGER_IND_USER_LOG_FILENAME = "gryf-ind";

    @Autowired
    HttpServletRequest request;

    @Pointcut("execution(* pl.sodexo.it.gryf.web.ind.controller..*.*(..))")
    public void indWebModule() {
    }

    @Pointcut("execution(* pl.sodexo.it.gryf.web.ti.controller..*.*(..))")
    public void tiWebModule() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllers() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllers() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethods() {
    }

    @After("publicMethods() && (controllers() || restControllers()) && tiWebModule()")
    public void getTiLogs(JoinPoint joinPoint) {
        String ipAddress = getIpAddress();
        MDC.put(LOGGER_USER_LOG_FILENAME_PLACEHOLDER, LOGGER_TI_USER_LOG_FILENAME);
        LOGGER.info("Żądanie z adresu IP={}, url={}, parametry wywołania={}", ipAddress, request.getRequestURL(), joinPoint.getArgs());
    }

    @After("publicMethods() && (controllers() || restControllers()) && indWebModule()")
    public void getIndLogs(JoinPoint joinPoint) {
        String ipAddress = getIpAddress();
        MDC.put(LOGGER_USER_LOG_FILENAME_PLACEHOLDER, LOGGER_IND_USER_LOG_FILENAME);
        LOGGER.info("Żądanie z adresu IP={}, url={}, parametry wywołania={}", ipAddress, request.getRequestURL(), joinPoint.getArgs());
    }

    private String getIpAddress() {
        String ipAddress = request.getHeader(X_FORWARDED_FOR);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
