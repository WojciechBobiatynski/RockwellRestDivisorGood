package pl.sodexo.it.gryf.common.exception.accounts;

import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;

/**
 * Wyjątek dla błędnie wpisanego kodu osoby fizycznej
 *
 * Created by akmiecinski on 02.11.2016.
 */
public class GryfInvalidAccountCode extends GryfRuntimeException {

    public GryfInvalidAccountCode(String message) {
        super(message);
    }
}
