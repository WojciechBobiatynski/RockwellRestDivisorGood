package pl.sodexo.it.gryf.exception;

public class InvalidObjectIdException extends RuntimeException {

    public InvalidObjectIdException() {
    }

    public InvalidObjectIdException(String message) {
        super(message);
    }

    public InvalidObjectIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectIdException(Throwable cause) {
        super(cause);
    }

    public InvalidObjectIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    

}
