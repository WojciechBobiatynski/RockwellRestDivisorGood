package pl.sodexo.it.gryf.common.exception.authentication;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjatek rzucany w przypadku blednych danych podczas autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
public class GryfBadCredentialsException extends GryfRuntimeException {

    public GryfBadCredentialsException(String message) {
        super(message);
    }

    public GryfBadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
