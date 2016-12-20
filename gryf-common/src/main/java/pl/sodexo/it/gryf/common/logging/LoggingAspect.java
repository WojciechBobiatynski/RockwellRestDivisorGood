package pl.sodexo.it.gryf.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Aspekt automatycznie logujacy wejscie i wyjscie metod wskazanych w pointcutach.
 *
 * Created by adziobek on 26.09.2016.
 */
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
    private LoggingLevel loggingLevel = LoggingLevel.TRACE;
    private boolean logOutput = true;

    @Pointcut("execution(* @org.springframework.stereotype.Service pl.sodexo.it.gryf.service..*.*(..))"
            + " && !execution(* pl.sodexo.it.gryf.service.impl.dictionaries..*.*(..))"
            + " && !execution(* pl.sodexo.it.gryf.service.impl.security..*.*(..))" + " && !execution(* pl.sodexo.it.gryf.service.impl.other..*(..))")
    public void executionOfAnyServiceMethod() {
    }

    @Pointcut("!@annotation(pl.sodexo.it.gryf.common.annotation.LoggingDisabled)")
    public void enabled() {
    }

    @Before("executionOfAnyServiceMethod() && enabled()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {
        createLogBefore(joinPoint);
    }

    @AfterReturning(pointcut = "executionOfAnyServiceMethod() && enabled()", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result) {
        createLogAfter(joinPoint, result);
    }

    private void createLogBefore(JoinPoint joinPoint) {
        Method method = retrieveTargetMethodFrom(joinPoint);
        LoggingConfig methodConfig = resolveLoggingConfig(joinPoint, method);
        doLoggingBefore(joinPoint, method, methodConfig);
    }

    private void createLogAfter(JoinPoint joinPoint, Object result) {
        Method method = retrieveTargetMethodFrom(joinPoint);
        LoggingConfig methodConfig = resolveLoggingConfig(joinPoint, method);

        if (!methodConfig.logOutput()) return;

        StringBuilder stringBuilder = new StringBuilder();
        doLoggingDefault(joinPoint, method, stringBuilder);
        stringBuilder.append(" [OUTPUT]: ");
        stringBuilder.append(String.valueOf(result));
        LOGGER.info(stringBuilder.toString());
    }

    private void doLoggingBefore(JoinPoint joinPoint, Method method, LoggingConfig methodConfig) {
        if (method == null || methodConfig == null) return;
        if (LoggingLevel.NONE.equals(methodConfig.value())) return;

        StringBuilder stringBuilder = new StringBuilder();
        if (LoggingLevel.DEFAULT.equals(methodConfig.value())) {
            doLoggingDefault(joinPoint, method, stringBuilder);
        } else if (LoggingLevel.TRACE.equals(methodConfig.value())) {
            doLoggingTrace(joinPoint, method, stringBuilder);
        }
        LOGGER.info(stringBuilder.toString());
    }

    private void doLoggingDefault(JoinPoint joinPoint, Method method, StringBuilder stringBuilder) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        stringBuilder.append(className)
        .append(".")
        .append(methodName)
        .append("()");
    }

    private void doLoggingTrace(JoinPoint joinPoint, Method method, StringBuilder stringBuilder) {
        doLoggingDefault(joinPoint, method, stringBuilder);

        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = sig.getParameterNames();
        Class[] parameterTypes = sig.getParameterTypes();
        Object[] parameters = joinPoint.getArgs();
        Parameter[] paramsDef = method.getParameters();
        try {
            Annotation[][] parameterAnnotations = joinPoint.getTarget().getClass().getMethod(method.getName(), parameterTypes).getParameterAnnotations();
        } catch (NoSuchMethodException e) {
            stringBuilder.append(e.getMessage());
        }

        stringBuilder.append(" [INPUT]: ");
        for (int i = 0; i < parameters.length; i++) {
            LoggingConfig annotation = paramsDef[i].getAnnotation(LoggingConfig.class);
            NoLog noLog = paramsDef[i].getAnnotation(NoLog.class);

            if (annotation != null && LoggingLevel.NONE.equals(annotation.value())) continue;
            if(noLog != null) continue;

            String parameterName = String.valueOf(parameterNames != null ? parameterNames[i] : null);
            stringBuilder.append("(P")
            .append(i + 1)
            .append(")")
            .append(parameterName)
            .append("=")
            .append(String.valueOf(parameters[i]))
            .append(" ,");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
    }

    /**
     * Pobranie adnotacji z klasy a nastepnie z metody.
     * Adnotacja na metodzie nadpisuje anotacje na klasie
     *
     * @return LoggingConfig - dla metody lub klasy lub config domyslny
     */
    private LoggingConfig resolveLoggingConfig(JoinPoint joinPoint, Method method) {
        LoggingConfig annotation = null;
        NoLog noLog = null;
        if (joinPoint.getTarget().getClass().isAnnotationPresent(LoggingConfig.class)) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(LoggingConfig.class);
        }
        if (joinPoint.getTarget().getClass().isAnnotationPresent(NoLog.class)) {
            noLog = joinPoint.getTarget().getClass().getAnnotation(NoLog.class);
        }
        if (method != null && method.isAnnotationPresent(LoggingConfig.class)) {
            annotation = method.getAnnotation(LoggingConfig.class);
        }
        if (method != null && method.isAnnotationPresent(NoLog.class)) {
            noLog = method.getAnnotation(NoLog.class);
        }
        if (annotation == null) {
            annotation = getDefaultConfig(noLog != null);
        }
        return annotation;
    }

    /**
     * Pobieranie definicji wywoływanej metody.
     */
    private Method retrieveTargetMethodFrom(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getMethod().getName();
            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
            return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.warn("No message: " + joinPoint.getSignature().getName(), e);
        }
        return null;
    }

    private LoggingConfig getDefaultConfig(boolean noLog) {
        return new LoggingConfig() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return LoggingConfig.class;
            }
            @Override
            public LoggingLevel value() {
                return noLog ? LoggingLevel.NONE : loggingLevel;
            }
            @Override
            public boolean logOutput() {
                return !noLog && logOutput;
            }
        };
    }
}
