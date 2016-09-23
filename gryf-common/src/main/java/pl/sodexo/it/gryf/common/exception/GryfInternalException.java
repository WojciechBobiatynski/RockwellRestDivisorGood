package pl.sodexo.it.gryf.common.exception;

/**
 * Wyjątek wewnętrzny aplikacji.
 * 
 * Created by akuchna on 2016-09-22.
 */
public class GryfInternalException extends GryfRuntimeException {

    public GryfInternalException(String message) {
        super(message);
    }

    public GryfInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
