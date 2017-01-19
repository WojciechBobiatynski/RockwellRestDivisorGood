package pl.sodexo.it.gryf.common.exception;

/**
 * Wyjątek rzucany gdy brakuje danych, które powinny być przechowywane w bazie
 *
 * Created by akmiecinski on 19.01.2016.
 */
public class NoAppropriateData extends GryfRuntimeException{

    public NoAppropriateData(String message) {
        super();
        this.message = message;
    }
}
