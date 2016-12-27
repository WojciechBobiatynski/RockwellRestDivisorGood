package pl.sodexo.it.gryf.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;

/**
 * Aspekt obsługujący wyjatki warstwy logiki biznesowej.
 * Wrapuje OptimisticLockException wyjątkiem aplikacyjnym GryfOptimisticLockRuntimeException.
 *
 * Created by jbentyn on 2016-09-21.
 */
@Aspect
@Order(1)
public class HandleExceptionAspect {

    @Around("execution(* pl.sodexo.it.gryf.service.impl..*.*(..))")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable { // NOSONAR
        try {
            return pjp.proceed();
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException e) {
            throw new GryfOptimisticLockRuntimeException(e);
        }
    }
}
