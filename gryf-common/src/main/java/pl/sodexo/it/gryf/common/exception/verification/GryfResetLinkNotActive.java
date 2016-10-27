package pl.sodexo.it.gryf.common.exception.verification;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek dla sytuacji gdy link resetowania hasła wygasł lub został już wykorzystany
 *
 * Created by akmiecinski on 26.10.2016.
 */
public class GryfResetLinkNotActive  extends GryfRuntimeException {

    public GryfResetLinkNotActive(String message) {
        super(message);
    }

}
