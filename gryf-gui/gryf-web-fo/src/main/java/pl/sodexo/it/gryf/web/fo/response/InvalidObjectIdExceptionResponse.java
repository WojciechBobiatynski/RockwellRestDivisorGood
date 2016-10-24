package pl.sodexo.it.gryf.web.fo.response;

public class InvalidObjectIdExceptionResponse {

    private final ResponseType responseType = ResponseType.INVALID_OBJECT_ID_EXCEPTION;
    private final String message;

    public ResponseType getResponseType() {
        return responseType;
    }

    public InvalidObjectIdExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
