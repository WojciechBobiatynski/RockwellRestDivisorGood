package pl.sodexo.it.gryf.common.exception.accounts;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek rzucany, gdy dana para konto-umowa jest już zarezerwowana
 *
 * Created by akmiecinski on 02.11.2016.
 */
public class GryfAccountUsed extends GryfRuntimeException {

    public GryfAccountUsed(String message) {
        super(message);
    }
}
