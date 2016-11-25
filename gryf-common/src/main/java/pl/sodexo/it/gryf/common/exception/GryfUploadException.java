package pl.sodexo.it.gryf.common.exception;

/**
 * Klasa wyjątków związana z uploadem plików
 *
 * Created by akmiecinski on 24.11.2016.
 */
public class GryfUploadException extends GryfRuntimeException {

    public GryfUploadException(String message) {
        super(message);
    }

    public GryfUploadException(String message, Throwable cause) {
        super(message, cause);
    }

}
