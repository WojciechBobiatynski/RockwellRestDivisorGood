package pl.sodexo.it.gryf.common.exception;

/**
 * Klasa dla wyjątków walidacji
 *
 * Created by akmiecinski on 26.01.2017.
 */
public class GryfValidationException extends GryfRuntimeException {

    public GryfValidationException(String message) {
        super(message);
        this.message = message;
    }
}
