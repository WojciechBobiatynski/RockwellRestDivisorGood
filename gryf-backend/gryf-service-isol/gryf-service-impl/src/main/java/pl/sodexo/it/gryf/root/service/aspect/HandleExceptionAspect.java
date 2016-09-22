package pl.sodexo.it.gryf.root.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;

/**
 * Aspekt obsługujący wyjatki warstwy logiki biznesowej.
 * Wrapuje OptimisticLockException wyjątkiem aplikacyjnym GryfOptimisticLockRuntimeException.
 *
 * Created by jbentyn on 2016-09-21.
 */
//TODO na razie wyłaczam
//@Aspect
//@Order(1)
public class HandleExceptionAspect {

//    @Around("execution(* pl.sodexo.it.gryf.root.service.impl..*.*(..))")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable { // NOSONAR
        try {
            return pjp.proceed();
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException  e) {
            throw new GryfOptimisticLockRuntimeException("OptimisticLock w trakcie operacji na bazie", e);
        }
    }
}
