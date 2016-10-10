package pl.sodexo.it.gryf.common.exception.authentication;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany gdy hasło uzytkownika wygasło
 *
 * Created by jbentyn on 2016-10-06.
 */
public class GryfPasswordExpiredException extends GryfRuntimeException {

    public GryfPasswordExpiredException(String message) {
        super(message);
    }

    public GryfPasswordExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
