package pl.sodexo.it.gryf.common.exception.authentication;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjatek rzucany w przypadku bledu podczas autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
public class GryfAuthenticationException extends GryfRuntimeException {

    public GryfAuthenticationException(String message) {
        super(message);
    }

    public GryfAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
