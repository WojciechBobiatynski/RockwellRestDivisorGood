package pl.sodexo.it.gryf.common.exception.publicbenefits.orders;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-27.
 */
public class ServiceInstantiationException extends TypeNotPresentException {
    private String messageText;
    /**
     * Constructs a <tt>TypeNotPresentException</tt> for the named type
     * with the specified cause.
     *
     * @param typeName the fully qualified name of the unavailable type
     * @param cause    the exception that was thrown when the system attempted to
     */
    public ServiceInstantiationException(String messageText, String typeName, Throwable cause) {
        super(typeName, cause);
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }
}
