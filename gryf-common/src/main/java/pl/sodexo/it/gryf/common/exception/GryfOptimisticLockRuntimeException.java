package pl.sodexo.it.gryf.common.exception;

/**
 * Wyjatek rzucany przy obsludze konkurencyjnych modyfikacji rekordow na bazie danych.
 *
 * Created by jbentyn on 2016-09-21.
 */
public class GryfOptimisticLockRuntimeException extends GryfRuntimeException {

    private final static String DEFAULT_MESSAGE = "Dane zostały zmodyfikowane przez innego użytkownika. "
                                                + "Należy odswieżyć dane i spróbować ponownie.";

    public GryfOptimisticLockRuntimeException() {
        super(DEFAULT_MESSAGE);
    }

    public GryfOptimisticLockRuntimeException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
