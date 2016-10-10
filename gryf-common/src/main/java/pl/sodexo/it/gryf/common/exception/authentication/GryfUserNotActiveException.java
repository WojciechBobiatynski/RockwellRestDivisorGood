package pl.sodexo.it.gryf.common.exception.authentication;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany gdy uzytkownik nie jest aktywny
 *
 * Created by jbentyn on 2016-10-06.
 */
public class GryfUserNotActiveException extends GryfRuntimeException {

    public GryfUserNotActiveException(String message) {
        super(message);
    }

    public GryfUserNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
