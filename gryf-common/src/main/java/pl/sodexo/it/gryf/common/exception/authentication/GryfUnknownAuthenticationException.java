package pl.sodexo.it.gryf.common.exception.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * Wyjątek rzucany gdy mamy problemy z autentykacją. Stworzony na potrzeby wykorzystania defaultowego mechanizmu spring security
 *
 * Created by akmiecinski on 25.01.2017.
 */
public class GryfUnknownAuthenticationException extends AuthenticationException {

    public GryfUnknownAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public GryfUnknownAuthenticationException(String msg) {
        super(msg);
    }
}
