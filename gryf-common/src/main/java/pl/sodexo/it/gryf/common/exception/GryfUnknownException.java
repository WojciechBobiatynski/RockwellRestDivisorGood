package pl.sodexo.it.gryf.common.exception;

/**
 *  Wyjatek rzucany w przypadku nieznanego bledu.
 * 
 * Created by akuchna on 2016-09-26.
 */
public class GryfUnknownException extends GryfRuntimeException {

    public GryfUnknownException(String message) {
        super(message);
    }

    public GryfUnknownException(String message, Throwable cause) {
        super(message, cause);
    }
}
