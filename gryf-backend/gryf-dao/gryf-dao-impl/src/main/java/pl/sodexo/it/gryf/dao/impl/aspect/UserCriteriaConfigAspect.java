package pl.sodexo.it.gryf.dao.impl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.annotation.ConfigInjectable;
import pl.sodexo.it.gryf.common.api.ConfigSettable;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;

import java.util.Arrays;

/**
 * Aspekt odpowiedzialny za uzupelnienie obiektu AzpConfiguration w obiekcie UserCriteria
 * 
 * @author ekaczynski@isolution.pl
 *         created: 13 sty 2015 07:42:21
 */
@Aspect
public class UserCriteriaConfigAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCriteriaConfigAspect.class);
    
    @Autowired
    private ApplicationParameters applicationParameters;
    
    @Around("execution(* pl.sodexo.it.gryf.dao.api.search.mapper..*.*(..))")
    public Object setConfig(ProceedingJoinPoint pjp) throws Throwable { // NOSONAR
        addConfig(pjp.getArgs());
        return pjp.proceed();
    }

    /**
     * Dodanie konfiguracji do pola typu UserCriteria
     */
    private void addConfig(Object[] args) {
        if(args == null || args.length == 0){
            return;
        }

        Arrays.stream(args).forEach(arg -> {
            if(arg == null){
                return;
            }

            Class<?> argClass = arg.getClass();
            LOGGER.trace("Klasa parametru, class={}", argClass);
            if (argClass.isAnnotationPresent(ConfigInjectable.class)) {
                LOGGER.trace("Ustawienie ApplicationParameters w kryteriach");
                ConfigSettable configSettable = (ConfigSettable) arg;
                configSettable.setConfig(applicationParameters);
            }
        });
    }
    
}
