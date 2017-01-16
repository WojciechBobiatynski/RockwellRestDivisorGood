package pl.sodexo.it.gryf.common.exception;

/**
 *  Wyjatek rzucany w przypadku błędów związanych z zapisem plików
 * 
 * Created by akmiecinski on 2017-01-16.
 */
public class GryfFileException extends GryfRuntimeException {

    public GryfFileException(String message) {
        super(message);
    }

    public GryfFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
