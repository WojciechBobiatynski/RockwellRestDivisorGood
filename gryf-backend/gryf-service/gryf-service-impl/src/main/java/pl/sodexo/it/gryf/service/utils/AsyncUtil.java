package pl.sodexo.it.gryf.service.utils;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Executor;

/**
 * Util do operacji asynchronicznych
 *
 * Created by jbentyn on 2016-10-14.
 */
public final  class AsyncUtil {

    private AsyncUtil(){

    }

    /**
     * Zwraca Executor, który uruchamia zadania w sposób asynchroniczny
     * i przekazuje do tworzonych przez siebie watków kontekst zalogowanego uzytkownika
     *
     * @return asynchroniczny executor z załadowanym kontekstem uzytkownika
     */
    public static Executor getDelegatingSecurityContextAsyncExecutor (){
        SimpleAsyncTaskExecutor delegateExecutor = new SimpleAsyncTaskExecutor();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        context.setAuthentication(authentication);
        return new DelegatingSecurityContextExecutor(delegateExecutor, context);
    }
}
