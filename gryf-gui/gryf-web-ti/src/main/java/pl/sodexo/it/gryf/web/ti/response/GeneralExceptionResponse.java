package pl.sodexo.it.gryf.web.ti.response;

public class GeneralExceptionResponse {

    private final ResponseType responseType = ResponseType.GENERAL_EXCEPTION;
    private final String message;
    private final String cause;
    private final String methodName;
    private final String className;
    private final Integer lineNumber;


    public ResponseType getResponseType() {
        return responseType;
    }

    public GeneralExceptionResponse(String message, String cause, String methodName, String className, Integer lineNumber) {
        this.message = message;
        this.cause = cause;
        this.methodName = methodName;
        this.className = className;
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }
}
