package pl.sodexo.it.gryf.common.exception.verification;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany gdy użytkownik TI poda pesel kod weryfikacyjny użytkownika, które nie identyfikują żadnego uczestnika
 *
 * Created by akmiecinski on 26.10.2016.
 */
public class GryfIndUserVerificationException extends GryfRuntimeException {

    public GryfIndUserVerificationException(String message) {
        super(message);
    }

}
