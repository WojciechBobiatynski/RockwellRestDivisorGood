package pl.sodexo.it.gryf.common.exception.verification;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany dla problemów z captchą
 *
 * Created by akmiecinski on 27.10.2016.
 */
public class GryfCaptchaException extends GryfRuntimeException{

    public GryfCaptchaException(String message) {
        super(message);
    }
}
