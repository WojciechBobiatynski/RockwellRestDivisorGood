package pl.sodexo.it.gryf.web.ti.response;

public class IndUserVerificationExceptionResponse {

    private final ResponseType responseType = ResponseType.IND_USER_VERIFICATION_EXCEPTION;
    private final String message;

    public ResponseType getResponseType() {
        return responseType;
    }

    public IndUserVerificationExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
