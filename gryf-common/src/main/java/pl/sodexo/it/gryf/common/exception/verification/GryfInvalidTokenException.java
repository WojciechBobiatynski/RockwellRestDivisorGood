package pl.sodexo.it.gryf.common.exception.verification;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany gdy użytkownik ręcznie zmanipuluje link resetu hasła
 *
 * Created by akmiecinski on 26.10.2016.
 */
public class GryfInvalidTokenException extends GryfRuntimeException {

    public GryfInvalidTokenException(String message) {
        super(message);
    }

}
