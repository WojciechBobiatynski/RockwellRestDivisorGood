package pl.sodexo.it.gryf.common.exception;

/**
 * Wyjatek rzucany przy obsludze konkurencyjnych modyfikacji rekordow na bazie danych.
 *
 * Created by jbentyn on 2016-09-21.
 */
public class GryfOptimisticLockRuntimeException extends GryfRuntimeException {

    public GryfOptimisticLockRuntimeException(String message) {
        super(message);
    }

    public GryfOptimisticLockRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
