package pl.sodexo.it.gryf.common.exception.verification;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek dla sytuacji podania błędnej pary PESEL - email przy ponownym wysłaniu kodu weryfikacyjnego
 *
 * Created by akmiecinski on 17.10.2016.
 */
public class GryfVerificationException extends GryfRuntimeException {

    public GryfVerificationException(String message) {
        super(message);
    }

}
