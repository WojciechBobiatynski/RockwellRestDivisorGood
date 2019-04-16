package pl.sodexo.it.gryf.common.exception;

/**
 * Klasa wspólna dla wyjątków aplikacji Gryf.
 *
 * Created by jbentyn on 2016-09-21.
 */
public abstract class GryfRuntimeException extends RuntimeException {

    protected String message;

    public GryfRuntimeException(){
        super();
    }

    public GryfRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    public GryfRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
