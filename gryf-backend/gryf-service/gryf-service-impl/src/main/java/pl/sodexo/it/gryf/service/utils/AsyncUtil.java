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

    public static Executor getDelegatingSecurityContextAsyncExecutor (){
        SimpleAsyncTaskExecutor delegateExecutor = new SimpleAsyncTaskExecutor();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        context.setAuthentication(authentication);
        return new DelegatingSecurityContextExecutor(delegateExecutor, context);
    }
}
