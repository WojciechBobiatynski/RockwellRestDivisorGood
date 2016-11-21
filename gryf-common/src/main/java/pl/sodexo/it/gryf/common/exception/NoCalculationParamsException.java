package pl.sodexo.it.gryf.common.exception;

/**
 * Wyjątek rzucany w momencie braku odpowiednich parametrów do obliczania kwot rozliczenia
 *
 * Created by akmiecinski on 21.11.2016.
 */
public class NoCalculationParamsException extends GryfRuntimeException{

    public NoCalculationParamsException() {
        super();
        this.message = "Nie udało się znaleźć parametrów potrzebnych do wyliczenia pól rozliczenia";
    }
}
